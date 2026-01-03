package org.mihok.csv.parser.dto;

import org.mihok.csv.parser.CsvColumn;
import org.mihok.csv.parser.CsvSchema;
import org.mihok.csv.parser.NotBlank;

@CsvSchema
public class Wafer {
    private Long id;

    @CsvColumn(name = "fab")
    private String fab;

    @NotBlank
    @CsvColumn(name = "vendor_code")
    private String vendorCode;

    public String getFab() {
        return this.fab;
    }

    public String getVendorCode() {
        return this.vendorCode;
    }

    @Override
    public String toString() {
        return "Wafer[fab=" + this.fab + "]";
    }
}
