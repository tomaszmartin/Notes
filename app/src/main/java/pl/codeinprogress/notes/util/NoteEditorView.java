package pl.codeinprogress.notes.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NoteEditorView extends WebView {

    private String currentContent;
    private String currentTitle;

    @SuppressLint("SetJavaScriptEnabled")
    public NoteEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        loadUrl("file:///android_asset/note.html");
    }

    public NoteEditorView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    private void evaluate(String javascript) {
        evaluateJavascript("javascript:editor." + javascript + ";", text -> {
            String all = text.replaceFirst("re-callback://", "");
            String[] contents = all.split("<!--pl.codeinprogress.note.noteview.divider--!>");

            if (contents.length > 0) {
                currentTitle = contents[0];
            }

            if (contents.length > 1) {
                currentContent = contents[1];
            }
        });
    }

    public String getContent() {
        return currentContent;
    }

    public String getTitle() {
        return currentTitle;
    }

    public void setContent(@NonNull String html) {
        currentContent = html;
        evaluate("setHtml('" + html + "')");
    }

    public void setTitle(@NonNull String title) {
        currentTitle = title;
        evaluate("setTitle('" + title + "')");
    }

    public void setFontSize(int px) {
        evaluate("setFontSize('" + px + "px')");
    }

    public void toggleBold() {
        evaluate("toggleBold()");
    }

    public void toggleItalic() {
        evaluate("toggleItalic()");
    }

    public void toggleStrike() {
        evaluate("toggleStrike()");
    }

    public void toggleUnderline() {
        evaluate("toggleUnderline()");
    }

    public void setTextColor(String hex) {
        evaluate("setTextColor('" + hex + "')");
    }

    public void setTextBackgroundColor(String hex) {
        evaluate("setTextColor('" + hex + "')");
    }

    public void setHeader(int heading) {
        evaluate("setHeader('" + heading + "')");
    }

    public void setBulletedList() {
        evaluate("setBulletedList()");
    }

    public void setNumberedList() {
        evaluate("setNumberedList()");
    }

    public void insertImage(String url) {
        evaluate("insertImage('" + url + "')");
    }

}
