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
package au.org.ala.delta.directives;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.DeltaContext.PrintActionType;
import au.org.ala.delta.directives.args.DirectiveArguments;

/**
 * Processes the PRINT UNCODED CHARACTERS directive.
 */
public class TranslateUncodedCharacters extends AbstractNoArgDirective {

	public TranslateUncodedCharacters() {
		super("translate", "uncoded", "characters");
	}
	
	@Override
	public void process(DeltaContext context, DirectiveArguments directiveArguments) throws Exception {
		context.addPrintAction(PrintActionType.TRANSLATE_UNCODED_CHARACTERS);
	}
	
	@Override
	public int getOrder() {
		return 4;
	}
}
