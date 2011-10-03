package au.org.ala.delta.translation.dist;

import au.org.ala.delta.DeltaContext;
import au.org.ala.delta.dist.WriteOnceDistItemsFile;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.model.format.CharacterFormatter;
import au.org.ala.delta.model.format.ItemFormatter;
import au.org.ala.delta.translation.DataSetTranslator;
import au.org.ala.delta.translation.FilteredDataSet;

/**
 * Translates a DELTA data set into the format used by the DIST program.
 */
public class DistTranslator implements DataSetTranslator {

	private DeltaContext _context;
	private FilteredDataSet _dataSet;
	private CharacterFormatter _characterFormatter;
	private ItemFormatter _itemFormatter;
	
	public DistTranslator(DeltaContext context, FilteredDataSet dataSet, ItemFormatter itemFormatter, CharacterFormatter characterFormatter) {
		_context = context;
		_dataSet = dataSet;
		_characterFormatter = characterFormatter;
		_itemFormatter = itemFormatter;
	}

	@Override
	public void translateCharacters() {
		throw new UnsupportedOperationException("Dist character translation is not supported");
	}
	
	@Override
	public void translateItems() {
		String fileName = _context.getOutputFileSelector().getDistOutputFilePath();
		
		WriteOnceDistItemsFile itemsFile = new WriteOnceDistItemsFile(
				_dataSet.getMaximumNumberOfItems(), _dataSet.getNumberOfCharacters(), fileName, BinFileMode.FM_APPEND);
		DistItemsFileWriter itemsWriter = new DistItemsFileWriter(_context, _dataSet, _itemFormatter, itemsFile);
		itemsWriter.writeAll();
		itemsFile.close();
	}
}