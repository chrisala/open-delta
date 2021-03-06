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
package au.org.ala.delta.ui.image.overlay;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.application.ResourceMap;

import au.org.ala.delta.model.Illustratable;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import au.org.ala.delta.model.NumericCharacter;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.Formatter.CommentStrippingMode;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.model.format.Formatter.AngleBracketHandlingMode;
import au.org.ala.delta.model.image.ImageOverlay;
import au.org.ala.delta.model.image.ImageSettings;
import au.org.ala.delta.model.image.OverlayType;

/**
 * Determines the appropriate text to display on an image overlay.
 */
public class OverlayTextBuilder {

	private ItemFormatter _itemFormatter;
	private CharacterFormatter _characterFormatter;
	private CharacterFormatter _stateFormatter;
	private ResourceMap _resources;
	private ImageSettings _imageSettings;

	public OverlayTextBuilder(ResourceMap resources, ImageSettings imageSettings) {
		_resources = resources;
		_imageSettings = imageSettings;
		_itemFormatter = new ItemFormatter(false, CommentStrippingMode.RETAIN, AngleBracketHandlingMode.REMOVE, true, false, false);
		_characterFormatter = new CharacterFormatter(false, CommentStrippingMode.STRIP_ALL, AngleBracketHandlingMode.RETAIN, false, false);
		_stateFormatter = new CharacterFormatter(true, CommentStrippingMode.STRIP_ALL, AngleBracketHandlingMode.RETAIN, false, false);
	}

	public String getText(ImageOverlay overlay, Illustratable imageOwner) {
		String text = "";
		boolean includeExtraText = false;
		switch (overlay.type) {
		case OverlayType.OLTEXT: // Use a literal text string
			includeExtraText = true;
			break;
	    case OverlayType.OLKEYWORD: // Use specified keyword(s)
	        text = overlay.keywords;
	        includeExtraText = true;
	        break;
		case OverlayType.OLITEM: // Use name of the item
			if (!overlay.omitDescription()) {
				text = _itemFormatter.formatItemDescription((Item) imageOwner, overlay.includeComments() ? CommentStrippingMode.RETAIN : CommentStrippingMode.STRIP_ALL);
			}
			includeExtraText = true;
			break;
		case OverlayType.OLFEATURE: // Use name of the character
			if (!overlay.omitDescription()) {
				String description = _characterFormatter
						.formatCharacterDescription((au.org.ala.delta.model.Character) imageOwner, overlay.includeComments() ? CommentStrippingMode.RETAIN : CommentStrippingMode.STRIP_ALL);
				text = StringUtils.capitalize(description);
			}
			includeExtraText = true;
			break;
		case OverlayType.OLSTATE: // Use name of the state (selectable)
			if (!overlay.omitDescription()) {
				text = _stateFormatter.formatState(
						(MultiStateCharacter) imageOwner, overlay.stateId, overlay.includeComments() ? CommentStrippingMode.RETAIN : CommentStrippingMode.STRIP_ALL); // TODO convert from id to number inside slotfile code
			}
			includeExtraText = true;
			break;
		case OverlayType.OLVALUE: // Use specified values or ranges (selectable)
			if (!overlay.omitDescription()) {
				String value = overlay.getValueString();
				String units = getUnits(imageOwner, overlay);
				if (StringUtils.isNotEmpty(units)) {
					value += " " + units;
				}
				text = value;
			}
			includeExtraText = true;
			break;
		case OverlayType.OLUNITS: // Use units (for numeric characters)
			if (!overlay.omitDescription()) {
				text = getUnits(imageOwner, overlay);
			}
			includeExtraText = true;
			break;
		case OverlayType.OLOK: // Create OK pushbutton
			text = _resources.getString("imageOverlay.okButton.text");
			break;
		case OverlayType.OLCANCEL: // Create Cancel pushbutton
			text = _resources.getString("imageOverlay.cancelButton.text");
			break;
		case OverlayType.OLNOTES: // Create Notes pushbutton (for character notes)
			text = _resources.getString("imageOverlay.notesButton.text");
			break;
		case OverlayType.OLIMAGENOTES: // Create Notes pushbutton (for notes about the image)
			text = _resources.getString("imageOverlay.imageNotesButton.text");
			break;
	    case OverlayType.OLHEADING: // Using heading string for the data-set
	        text = _imageSettings.getDatasetName();
		case OverlayType.OLENTER: // Create edit box for data entry
		case OverlayType.OLCOMMENT: // Not a "real" overlay type, but used to
									// save comments addressed to images rather than overlays
		case OverlayType.OLBUTTONBLOCK: // Used only when modifying aligned push-buttons
		case OverlayType.OLHOTSPOT: // Not a "real" overlay type; used for convenience in editing
		case OverlayType.OLNONE: // Undefined; the remaining values MUST  correspond with array OLKeywords.
		case OverlayType.OLSUBJECT: // Has text for menu entry
		case OverlayType.OLSOUND: // Has name of .WAV sound file
			break;
		default:
			text = "";
		}
		
		if (includeExtraText) {
			if (StringUtils.isNotEmpty(text)) {
				text += " ";
			}
			text += overlay.overlayText;
		}
		return text;
	}

	private String getUnits(Illustratable imageOwner, ImageOverlay overlay) {
		return _characterFormatter.formatUnits((NumericCharacter<?>) imageOwner, overlay.includeComments() ? CommentStrippingMode.RETAIN : CommentStrippingMode.STRIP_ALL);
	}

}
