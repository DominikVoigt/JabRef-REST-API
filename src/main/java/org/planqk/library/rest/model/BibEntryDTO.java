package org.planqk.library.rest.model;

import org.jabref.model.entry.BibEntry;

public class BibEntryDTO {
    public BibEntry entry;

    public BibEntryDTO(BibEntry entry) {
        this.entry = entry;
    }
}