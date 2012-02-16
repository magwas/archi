/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.JRProperties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.diagram.util.DiagramUtils;
import uk.ac.bolton.archimate.jasperreports.data.ArchimateModelDataSource;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.persistence.utils.FileUtils;


/**
 * Export Model to Jasper Reports Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportJasperReportsWizard extends Wizard {
    
    private boolean DELETE_TEMP_FILES = true;
    
    private IArchimateModel fModel;
    
    private ExportJasperReportsWizardPage1 fPage1;
    private ExportJasperReportsWizardPage2 fPage2;
    
    private File fMainTemplateFile;
    private File fExportFolder;
    private String fExportFileName;
    private String fReportTitle;
    private boolean fIsPDF, fIsHTML, fIsDOCX, fIsPPT, fIsODT, fIsRTF;
    
    public ExportJasperReportsWizard(IArchimateModel model) {
        setWindowTitle("Generate Jasper Reports");
        fModel = model;
    }
    
    @Override
    public void addPages() {
        fPage1 = new ExportJasperReportsWizardPage1(fModel);
        addPage(fPage1);
        fPage2 = new ExportJasperReportsWizardPage2();
        addPage(fPage2);
    }

    @Override
    public boolean performFinish() {
        fPage1.saveSettings();
        
        fMainTemplateFile = fPage2.getMainTemplateFile();
        
        // Check this exists
        if(fMainTemplateFile == null || !fMainTemplateFile.exists()) {
            MessageDialog.openError(getShell(), "File Error", "Template is not available.");
            return false;
        }
        
        fExportFolder = fPage1.getExportFolder();
        fExportFileName = fPage1.getExportFilename();
        fReportTitle = fPage1.getReportTitle();
        fIsPDF = fPage1.isExportPDF();
        fIsHTML = fPage1.isExportHTML();
        fIsDOCX = fPage1.isExportDOCX();
        fIsPPT = fPage1.isExportPPT();
        fIsODT = fPage1.isExportODT();
        fIsRTF = fPage1.isExportRTF();
        
        // Check valid dir and file name
        try {
            File file = new File(fExportFolder, fExportFileName);
            file.getCanonicalPath();
            fExportFolder.mkdirs();
        }
        catch(Exception ex) {
            MessageDialog.openError(getShell(), "File Error", "The folder or filename is incorrect.");
            return false;
        }
        
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
                try {
                    dialog.run(false, true, new IRunnableWithProgress() {
                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                export(monitor);
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                                MessageDialog.openError(getShell(), "Export Error", ex.getMessage());
                            }
                            finally {
                                monitor.done();
                            }
                        }
                    });
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        return true;
    }

    private void export(IProgressMonitor monitor) throws Exception {
        monitor.beginTask("Generating Reports", 11);
        
        // Temp Folder to store assets
        File tmpFolder = new File(fExportFolder, "tmp");
        tmpFolder.mkdirs();
        
        //System.out.println("Exporting: " + fModel.getName());

        monitor.subTask("Creating Views...");
        writeDiagrams(tmpFolder);
        monitor.worked(1);
        
        monitor.subTask("Creating Jasper Print...");
        JasperPrint jasperPrint = createJasperPrint(monitor, tmpFolder);
        monitor.worked(1);
        
        if(fIsHTML) {
            monitor.subTask("Generating HTML...");
            exportHTML(jasperPrint, new File(fExportFolder, fExportFileName + ".html"));
        }
        monitor.worked(1);

        if(fIsPDF) {
            monitor.subTask("Generating PDF...");
            exportPDF(jasperPrint, new File(fExportFolder, fExportFileName + ".pdf"));
        }
        monitor.worked(1);

        if(fIsDOCX) {
            monitor.subTask("Generating DOCX...");
            exportDOCX(jasperPrint, new File(fExportFolder, fExportFileName + ".docx"));
        }
        monitor.worked(1);
        
        if(fIsPPT) {
            monitor.subTask("Generating PPT...");
            exportPPT(jasperPrint, new File(fExportFolder, fExportFileName + ".pptx"));
        }
        monitor.worked(1);
        
        if(fIsRTF) {
            monitor.subTask("Generating RTF...");
            exportRTF(jasperPrint, new File(fExportFolder, fExportFileName + ".rtf"));
        }
        monitor.worked(1);
        
        if(fIsODT) {
            monitor.subTask("Generating ODT...");
            exportODT(jasperPrint, new File(fExportFolder, fExportFileName + ".odt"));
        }
        monitor.worked(1);
        
        if(DELETE_TEMP_FILES) {
            monitor.subTask("Cleaning up...");
            FileUtils.deleteFolder(tmpFolder);
        }
        monitor.worked(1);
        
        //System.out.println("Finished");
    }
    
    /**
     * Write the diagrams to temp files
     */
    private void writeDiagrams(File tmpFolder) {
        for(IDiagramModel dm : fModel.getDiagramModels()) {
            Image image = DiagramUtils.createImage(dm);
            String diagramName = dm.getId() + ".png";
            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                File file = new File(tmpFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
    }
    
    private JasperPrint createJasperPrint(IProgressMonitor monitor, File tmpFolder) throws JRException {
        // Set the location of the default Jasper Properties File
        File propsFile = new File(JasperReportsPlugin.INSTANCE.getPluginFolder(), "jasperreports.properties");
        System.setProperty(JRProperties.PROPERTIES_FILE, propsFile.getAbsolutePath());

        // Set the location of the Images
        System.setProperty("JASPER_IMAGE_PATH", tmpFolder.getPath());

        // Declare Parameters passed to JasperFillManager
        Map<String, Object> params = new HashMap<String, Object>();

        // Parameters referenced in Report
        params.put("REPORT_TITLE", fReportTitle);
        //params.put(JRParameter.REPORT_LOCALE, Locale.US);
        
        monitor.worked(1);
        
        // Compile Main Report
        //System.out.println("Compiling Main Report");
        monitor.subTask("Compiling...");
        JasperReport mainReport = JasperCompileManager.compileReport(fMainTemplateFile.getPath());
        
        // Compile sub-reports
        File folder = fMainTemplateFile.getParentFile();
        for(File file : folder.listFiles()) {
            if(!file.equals(fMainTemplateFile) && file.getName().endsWith(".jrxml")) {
                //System.out.println("Compiling Sub-Report: " + file);
                JasperReport jr = JasperCompileManager.compileReport(file.getPath());
                params.put(jr.getName(), jr);
            }
        }
        
        monitor.worked(1);
        
        // Fill Report
        //System.out.println("Filling Report");
        monitor.subTask("Filling...");
        return JasperFillManager.fillReport(mainReport, params, new ArchimateModelDataSource(fModel));
    }
    
    private void exportHTML(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to HTML: " + file);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, file.getPath());
    }

    private void exportPDF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to PDF: " + file);
        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath());
    }
    
    private void exportDOCX(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to DOCX: " + file);
        JRDocxExporter msWordexporter = new JRDocxExporter();
        msWordexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        msWordexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        msWordexporter.exportReport();
    }

    private void exportPPT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to MS PPT: " + file);
        JRPptxExporter msPPTexporter = new JRPptxExporter();
        msPPTexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        msPPTexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        msPPTexporter.exportReport();
    }

    private void exportODT(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to ODT: " + file);
        JROdtExporter odtExporter = new JROdtExporter();
        odtExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        odtExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        odtExporter.exportReport();
    }
    
    private void exportRTF(JasperPrint jasperPrint, File file) throws JRException {
        //System.out.println("Exporting to RTF: " + file);
        JRRtfExporter rtfExporter = new JRRtfExporter();
        rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, file.getPath());
        rtfExporter.exportReport();
    }
}
