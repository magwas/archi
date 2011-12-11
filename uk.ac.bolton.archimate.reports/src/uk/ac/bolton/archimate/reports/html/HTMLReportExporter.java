/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.reports.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import uk.ac.bolton.archimate.editor.diagram.util.DiagramUtils;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.utils.FileUtils;
import uk.ac.bolton.archimate.editor.utils.HTMLUtils;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;


/**
 * Export model to HTML report
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportExporter {
    
    private IArchimateModel fModel;
    
    private File fMainFolder;
    
    private OutputStreamWriter writer;
    
    public void export(IArchimateModel model) throws IOException {
        fMainFolder = askSaveFolder();
        if(fMainFolder == null) {
            return;
        }
        
        fModel = model;
        
        File file = createMainHTMLPage();
        
        // Open it in Browser
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            browser.openURL(file.toURI().toURL());
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
    }
    
    private File createMainHTMLPage() throws IOException {
        File file = new File(fMainFolder, "report.html");
        writer = new OutputStreamWriter(new FileOutputStream(file), "UTF16");
        
        writeHeader();
        
        writeBusinessElements();
        writeApplicationElements();
        writeTechnologyElements();
        writeConnectionElements();
        writeDiagrams();
        
        writeCloser();
        
        writer.close();
        
        return file;
    }
    
    private void writeHeader() throws IOException {
        String s = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"; //$NON-NLS-1$
        s += "<html>\n"; 
        s += "<head>\n";
        s += "<title>" + "Archi Report" + "</title>\n";
        
        s += "<style type=\"text/css\">\n";
        s += "table { border-collapse:collapse; }\n";
        s += "table, td, th { border:1px solid black; }\n";
        s += "</style>\n";
        
        s += "</head>\n";
        s += "<body style=\"font-family:Verdana; font-size:10pt;\">\n";
        s += "<h1>" + "Archi Report" + "</h1>\n"; 
        writer.write(s);
        
        writer.write("<br/>\n");
        writeModelSummary(fModel);
        writer.write("<br/>\n");
    }
    
    private void writeCloser() throws IOException {
        String s = "</body>\n";
        s += "</html>";
        writer.write(s);
    }
    
    private void writeBusinessElements() throws IOException {
        IFolder businessFolder = fModel.getFolder(FolderType.BUSINESS);
        String color = ColorFactory.convertRGBToString(ColorFactory.COLOR_BUSINESS.getRGB());
        
        // Business Actors
        List<EObject> list = new ArrayList<EObject>();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessActor());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessRole());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInterface());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
        writeElements(list, "Business Actors", color);
        
        // Business Functions
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessFunction());
        writeElements(list, "Business Functions", color);
        
        // Business Information
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessObject());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getRepresentation());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getMeaning());
        writeElements(list, "Business Information", color);
        
        // Business Processes
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessActivity());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessEvent());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInteraction());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessProcess());
        writeElements(list, "Business Processes", color);
        
        // Business Products
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getContract());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getProduct());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessService());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getValue());
        writeElements(list, "Business Products", color);
    }
    
    private void writeApplicationElements() throws IOException {
        IFolder applicationFolder = fModel.getFolder(FolderType.APPLICATION);
        String color = ColorFactory.convertRGBToString(ColorFactory.COLOR_APPLICATION.getRGB());
        
        // Applications
        List<EObject> list = new ArrayList<EObject>();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationComponent());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationFunction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInteraction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInterface());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationService());
        writeElements(list, "Applications", color);
        
        // Application Data
        list.clear();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getDataObject());
        writeElements(list, "Application Data", color);
    }
    
    private void writeTechnologyElements() throws IOException {
        IFolder technologyFolder = fModel.getFolder(FolderType.TECHNOLOGY);
        String color = ColorFactory.convertRGBToString(ColorFactory.COLOR_TECHNOLOGY.getRGB());
        
        // Infrastructures
        List<EObject> list = new ArrayList<EObject>();
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getArtifact());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getCommunicationPath());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getDevice());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNode());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNetwork());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureService());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getSystemSoftware());
        writeElements(list, "Infrastructures", color);
    }
    
    private void writeConnectionElements() throws IOException {
        IFolder connectionsFolder = fModel.getFolder(FolderType.CONNECTORS);
        String color = ColorFactory.convertRGBToString(ColorFactory.COLOR_DIAGRAM_MODEL_REF.getRGB());
        
        List<EObject> list = new ArrayList<EObject>();
        getElements(connectionsFolder, list, null);
        writeElements(list, "Connectors", color);
    }
    
    private void getElements(IFolder folder, List<EObject> list, EClass type) {
        for(EObject object : folder.getElements()) {
        	if(type == null) {
                list.add(object);
            }
        	else if(object.eClass() == type) {
                list.add(object);
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, list, type);
        }
    }
    
    private void writeElements(List<EObject> list, String title, String color) throws IOException {
        if(!list.isEmpty()) {
            writer.write("<h2>" + title + "</h2>\n");
            
            // Sort a *copy* of the List
            List<EObject> copy = new ArrayList<EObject>(list);
            sort(copy);
            
            for(EObject object : copy) {
                if(object instanceof IArchimateElement) {
                    writeTableElement((IArchimateElement)object, color);
                    writer.write("<p/>");
                }
            }
            
            writer.write("<br/>");
        }
    }
    
    private void writeModelSummary(IArchimateModel model) throws IOException {
    	writer.write("<table width=\"100%\" border=\"0\">\n");
    	
    	writer.write("<tr bgcolor=\"" + "#F0F0F0" + "\">\n");
    	String name = fModel.getName();
        if(!StringUtils.isSet(name)) {
        	name = "(unnamed model)";
        }
        else {
        	name = parseChars(name);
        }
        writer.write("<td width=\"20%\" valign=\"top\">Name</td>\n");
        writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n");
        writer.write("</tr>\n");
        
        writer.write("<tr>\n");
        String date = DateFormat.getDateTimeInstance().format(new Date());
        writer.write("<td valign=\"top\">Date</td>\n");
        writer.write("<td valign=\"top\">" + date + "</td>\n");
        writer.write("</tr>\n");
        
        writer.write("<tr>\n");
        String doc = StringUtils.safeString(model.getDocumentation());
        doc = parseCharsAndLinks(doc);
        writer.write("<td valign=\"top\">Purpose</td>\n");
        writer.write("<td valign=\"top\">" + doc + "</td>\n");
        writer.write("</tr>\n");
        
        writeProperties(model);
        
        writer.write("</table>\n");

    }
    
    private void writeTableElement(IArchimateElement element, String color) throws IOException {
    	writer.write("<table width=\"100%\" border=\"0\">\n");
    	
    	writer.write("<tr bgcolor=\"" + color + "\">\n");
        String name = StringUtils.safeString(element.getName());
        name = parseChars(name);
        writer.write("<td width=\"20%\" valign=\"top\">Name</td>\n");
        writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n");
        writer.write("</tr>\n");
        
        writer.write("<tr>\n");
        String type = ArchimateNames.getDefaultName(element.eClass());
        writer.write("<td valign=\"top\">Type</td>\n");
        writer.write("<td valign=\"top\">" + type + "</td>\n");
        writer.write("</tr>\n");
        
        writer.write("<tr>\n");
        String doc = StringUtils.safeString(element.getDocumentation());
        doc = parseCharsAndLinks(doc);
        writer.write("<td valign=\"top\">Documentation</td>\n");
        writer.write("<td valign=\"top\">" + doc + "</td>\n");
        writer.write("</tr>\n");
        
        writeProperties(element);
        
        writer.write("</table>\n");
    }
    
    private void writeProperties(IProperties element) throws IOException {
    	for(IProperty property : element.getProperties()) {
        	writer.write("<tr>\n");
        	String key = parseChars(property.getKey());
        	writer.write("<td valign=\"top\">" + key + "</td>\n");
        	String value = parseCharsAndLinks(property.getValue());
        	writer.write("<td valign=\"top\">" + value + "</td>\n");
        	writer.write("</tr>\n");
		}
    }
    
    private void writeDiagrams() throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }
        
        // Sort a *copy* of the List
        List<IDiagramModel> copy = new ArrayList<IDiagramModel>(fModel.getDiagramModels());
        sort(copy);

        Hashtable<IDiagramModel, String> table = saveDiagrams(copy);

        writer.write("<br/><br/><br/>\n");
        writer.write("<h2>" + "Views" + "</h2>\n");

        for(IDiagramModel dm : copy) {
            writer.write("<table width=\"100%\" border=\"0\">\n");

            writer.write("<tr bgcolor=\"" + "#e0e4e6" + "\">\n");
            String name = StringUtils.safeString(dm.getName());
            name = parseChars(name);
            writer.write("<td width=\"20%\" valign=\"top\">Name</td>\n");
            writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n");
            writer.write("</tr>\n");

            writer.write("<tr>\n");
            String doc = StringUtils.safeString(dm.getDocumentation());
            doc = parseCharsAndLinks(doc);
            writer.write("<td valign=\"top\">Documentation</td>\n");
            writer.write("<td valign=\"top\">" + doc + "</td>\n");
            writer.write("</tr>\n");

            writeProperties(dm);

            writer.write("</table>\n");

            writer.write("<img src=\"" + table.get(dm) + "\"" + "/>\n");
            writer.write("<br/><br/><br/><br/>\n");
        }
    }
    
    private Hashtable<IDiagramModel, String> saveDiagrams(List<IDiagramModel> list) {
        Hashtable<IDiagramModel, String> table = new Hashtable<IDiagramModel, String>();
        int i = 1;
        
        for(IDiagramModel dm : list) {
            Image image = DiagramUtils.createImage(dm);
            String diagramName = dm.getName();
            if(StringUtils.isSet(diagramName)) {
                diagramName = FileUtils.getValidFileName(diagramName);
                int j = 2;
                String s = diagramName + ".png";
                while(table.containsValue(s)) {
                    s = diagramName + "_" + j++ + ".png";
                }
                diagramName = s;
            }
            else {
                diagramName = "View " + i++ + ".png";
            }

            table.put(dm, diagramName);

            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                File file = new File(fMainFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
        
        return table;
    }
    
    private File askSaveFolder() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        dialog.setText("Report");
        dialog.setMessage("Choose a folder in which to generate the report.");
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        File folder = new File(path);
        if(folder.exists()) {
            String[] children = folder.list();
            if(children != null && children.length > 0) {
                boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Report",
                        "'" + folder +
                        "' is not empty. Are you sure you want to overwrite it?");
                if(!result) {
                    return null;
                }
            }
        }
        else {
            folder.mkdirs();
        }
        
        return folder;
    }
    
    private String parseCharsAndLinks(String s) {
        s = parseChars(s);
        s = parseLinks(s);
        return s;
    }
    
    private String parseChars(String s) {
        // Escape chars
        s = s.replaceAll("&", "&amp;"); // This first
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("\"", "&quot;");
        
        // CRs become breaks
        s = s.replaceAll("(\r\n|\r|\n)", "<br/>"); // This last
        
        return s;
    }
    
    private String parseLinks(String s) {
        Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(s);
        List<String> done = new ArrayList<String>();
        
        while(matcher.find()) {
            String group = matcher.group();
            if(!done.contains(group)) {
                done.add(group);
                s = s.replaceAll(group, "<a href=\"" + group + "\">" + group + "</a>");
            }
        }
        
        return s;
    }
    
    /**
     * Sort a *copy* of the List
     */
    private void sort(List<?> list) {
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof INameable && o2 instanceof INameable) {
                    String name1 = StringUtils.safeString(((INameable)o1).getName()).toLowerCase().trim();
                    String name2 = StringUtils.safeString(((INameable)o2).getName()).toLowerCase().trim();
                    return name1.compareTo(name2);
                }
                return 0;
            }
        });
    }
}
