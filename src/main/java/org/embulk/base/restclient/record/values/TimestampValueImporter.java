package org.embulk.base.restclient.record.values;

import org.embulk.spi.Column;
import org.embulk.spi.DataException;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.time.TimestampParser;

import org.embulk.base.restclient.record.ServiceRecord;
import org.embulk.base.restclient.record.ServiceValue;
import org.embulk.base.restclient.record.ValueImporter;
import org.embulk.base.restclient.record.ValueLocator;

public class TimestampValueImporter
        extends ValueImporter
{
    public TimestampValueImporter(Column column, ValueLocator valueLocator, TimestampParser timestampParser)
    {
        super(column, valueLocator);
        this.timestampParser = timestampParser;
    }

    @Override
    public void findAndImportValue(ServiceRecord record, PageBuilder pageBuilder)
    {
        try {
            ServiceValue value = findValue(record);
            if (value == null || value.isNull()) {
                pageBuilder.setNull(getColumnToImport());
            }
            else {
                pageBuilder.setTimestamp(getColumnToImport(), value.timestampValue(timestampParser));
            }
        } catch (Exception ex) {
            throw new DataException("Failed to import a value for column: " + getColumnToImport().getName() + " (" + getColumnToImport().getType().getName() + ")", ex);
        }
    }

    private final TimestampParser timestampParser;
}
