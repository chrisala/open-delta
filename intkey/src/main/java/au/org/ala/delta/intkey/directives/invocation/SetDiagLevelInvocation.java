/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.intkey.directives.invocation;

import au.org.ala.delta.intkey.model.IntkeyContext;
import au.org.ala.delta.intkey.ui.UIUtils;

public class SetDiagLevelInvocation extends BasicIntkeyDirectiveInvocation {

    private int _diagLevel;

    public void setDiagLevel(int diagLevel) {
        this._diagLevel = diagLevel;
    }

    @Override
    public boolean execute(IntkeyContext context) {
        if (_diagLevel >= 1) {
            context.setDiagLevel(_diagLevel);
        } else {
            context.getUI().displayErrorMessage(UIUtils.getResourceString("DiagLevelMustBeGtEqOne.error"));
        }
        return true;
    }

}
