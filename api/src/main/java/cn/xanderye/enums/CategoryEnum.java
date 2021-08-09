package cn.xanderye.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XanderYe
 * @description:
 * @date 2021/7/28 8:23
 */
public enum CategoryEnum {

    QQ("qq"),
    GROUP("group"),
    SELF("self");

    private String name;

    private CategoryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> getCategories() {
        CategoryEnum[] categoryEnums = CategoryEnum.values();
        List<String> categoryList = new ArrayList<>();
        for (CategoryEnum ce : categoryEnums) {
            categoryList.add(ce.getName());
        }
        return categoryList;
    }
}
