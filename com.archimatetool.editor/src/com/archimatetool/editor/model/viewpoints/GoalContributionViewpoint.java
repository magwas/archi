/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IArchimatePackage;


/**
 * Goal Contribution Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class GoalContributionViewpoint extends AbstractViewpoint {
    
    EClass[] allowed = new EClass[] {
            IArchimatePackage.eINSTANCE.getGoal(),
            IArchimatePackage.eINSTANCE.getPrinciple(),
            IArchimatePackage.eINSTANCE.getRequirement(),
            IArchimatePackage.eINSTANCE.getConstraint(),
            
            IArchimatePackage.eINSTANCE.getRealisationRelationship(),
            IArchimatePackage.eINSTANCE.getAggregationRelationship(),
            IArchimatePackage.eINSTANCE.getSpecialisationRelationship(),
            IArchimatePackage.eINSTANCE.getInfluenceRelationship(),
    };
    
    @Override
    public String getName() {
        return Messages.GoalContributionViewpoint_0;
    }

    @Override
    public int getIndex() {
        return GOAL_CONTRIBUTION_VIEWPOINT;
    }
    
    @Override
    public EClass[] getAllowedTypes() {
        return allowed;
    }
}