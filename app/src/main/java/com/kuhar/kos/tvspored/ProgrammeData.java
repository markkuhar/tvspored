package com.kuhar.kos.tvspored;

import android.widget.CheckBox;

/**
 * Created by Mark on 29. 12. 2016.
 */

public class ProgrammeData implements Comparable<ProgrammeData> {
    public String sortNumber;

    public String getStartTime() {
        return startTime;
    }

    public String startTime;
    public String endTime;
    public String title;
    public String description;



    public void notFound(){
        sortNumber = "N/A";
        startTime = "N/A";
        endTime = "N/A";
        title = "Trenutno se ne predavaja nobena oddaja";
        description = "Not found";
    }

    @Override
    public int compareTo(ProgrammeData o) {
        if (getStartTime() != null && o.getStartTime() != null) {
            return getStartTime().compareTo(o.getStartTime());
        }
        return 0;
    }
}
