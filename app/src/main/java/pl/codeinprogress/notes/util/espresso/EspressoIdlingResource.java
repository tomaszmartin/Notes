package pl.codeinprogress.notes.util.espresso;

public class EspressoIdlingResource {

    private static final String name = "global";
    private static SimpleCountingIdlingResource idlingResource = new SimpleCountingIdlingResource(name);

    public static void increment() {
        idlingResource.increment();
    }

    public static void decrement() {
        idlingResource.decrement();
    }

    public static SimpleCountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

}