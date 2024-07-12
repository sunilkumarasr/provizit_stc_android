package com.provizit.Utilities.daysview;

public class DayModel {
    private boolean isSelected;

    public DayModel( boolean isSelected) {
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
