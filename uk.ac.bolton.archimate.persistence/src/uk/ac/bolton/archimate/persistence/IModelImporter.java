/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.persistence;

import java.io.IOException;

/**
 * Exporter interface
 * 
 * @author Phillip Beauvoir
 */
public interface IModelImporter {

    void doImport() throws IOException;

}
