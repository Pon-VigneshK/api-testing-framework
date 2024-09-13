package org.gcit.annotations;

import org.gcit.enums.CategoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Framework Annotation is user built annotation which is annotated on top of test methods to log the author details and
 * category details to the extent report.<br>
 *
 * Runtime retention value indicate that this annotation will be available at run time for reflection operations.
 * </p>
 *
 * @date 2024-07-02
 * @author Pon Vignesh K
 * @version 1.0
 * @since 1.0<br>
 * @see org.gcit.tests;
 * @see org.gcit.enums.CategoryType
 */

@Retention(RUNTIME)
@Target(METHOD)
public @interface FrameworkAnnotation {
    /**
     * Store the authors who created the tests in a String array.
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public String[] author() default {};
    /**
     * Stores the category in the form of an Enum Array. Include the possible values in {@link org.gcit.enums.CategoryType}.
     * @author Pon Vignesh K
     * @date 2024-07-02
     * @version 1.0
     */
    public CategoryType[] category() default {};

}
