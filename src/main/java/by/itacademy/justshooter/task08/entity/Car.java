package by.itacademy.justshooter.task08.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MyTable(name = "car")
public class Car {

    /**
     * Name field.
     */
    @MyColumn(name = "name")
    private String name;

    /**
     * Color field.
     */
    @MyColumn(name = "color")
    private String color;

    /**
     * Price field.
     */
    @MyColumn(name = "price")
    private Integer price;
}
