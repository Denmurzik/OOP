package org.example.model;

/**
 * Обычная еда змейка вырастает на 1 сегмент.
 */
public class BasicFood extends Food {

    public BasicFood(Point position) {
        super(position);
    }

    @Override
    public int getGrowthValue() {
        return 1;
    }
}
