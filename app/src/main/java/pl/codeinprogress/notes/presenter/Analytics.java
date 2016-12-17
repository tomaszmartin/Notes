package pl.codeinprogress.notes.presenter;

public interface Analytics {

    void sendEvent(String category, String action, String label, long value);
    void sendScreen(String screenName);

}
