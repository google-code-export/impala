package classes;

import interfaces.EntryService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Autowired class
 * @author Phil Zoio
 */
public class AutowiredClass {

    private EntryService entryService;

    @Autowired
    public void setEntryService(EntryService entryService) {
        this.entryService = entryService;
    }

    public void useMessage() {
        System.out.println(entryService);
        System.out.println(entryService.getEntriesOfCount(0));
    }

}
