package nure.ananiev.nestorian.lab_task5.enums;


import nure.ananiev.nestorian.lab_task5.R;

public enum Importance {
    LOW(R.drawable.ic_importance_low, R.string.importance_low),
    MEDIUM(R.drawable.ic_importance_medium, R.string.importance_medium),
    HIGH(R.drawable.ic_importance_high, R.string.importance_high);

    private final int iconResId, titleRes;

    Importance(int iconResId, int titleRes) {
        this.iconResId = iconResId;
        this.titleRes = titleRes;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getTitleRes() {
        return titleRes;
    }
}
