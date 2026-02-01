package option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Interface used to represent an enumeration of options that a user could pick from.
 */
public interface Option {
    /**
     * Asks the user to select one of the {@code Option}s defined in the provided {@code Class}.
     * @param enumClass The {@code Class} from which the {@code Option} must be chosen
     * @param scanner The {@code Scanner} which is to be used to retrieve user input
     * @return The {@code Option} that the user chose
     * @param <T> A generic type that represents the type of {@code Class}es that can be used as a
     *           source of {@code Option}s to choose from
     */
    @SuppressWarnings("unchecked")
    static <T extends Enum<T> & Option> T selectOption(Class<T> enumClass, Scanner scanner) {
        System.out.println(EnumSet.allOf(enumClass).stream().map(Enum::toString)
                                   .collect(Collectors.joining(", ")));
        do {
            System.out.print("Please input one of the options: ");
            try {
                return (T) enumClass.getMethod("valueOf", Class.class, String.class)
                        .invoke(null, enumClass, scanner.nextLine());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException _) {
            }
        } while (true);
    }

    /**
     * Returns the public constructor of this {@code Option}.
     * @return The public constructor of this {@code Option}
     */
    Constructor<?> getConstructor();
}
