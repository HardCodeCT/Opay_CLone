package com.pay.opay;

import android.os.Handler;
import android.webkit.WebView;
import android.widget.Toast;

public class ResolveAccountName {

    private final WebView webView;
    private final Handler handler;

    public ResolveAccountName(WebView webView) {
        this.webView = webView;
        this.handler = new Handler();
    }

    private void clickSelect2Dropdown() {
        webView.evaluateJavascript(
                "var el = document.querySelector('.select2-selection');" +
                        "if (el) {" +
                        "  var r = el.getBoundingClientRect();" +
                        "  el.dispatchEvent(new MouseEvent('mousedown', {" +
                        "    clientX: r.left + r.width/2," +
                        "    clientY: r.top + r.height/2," +
                        "    bubbles:true," +
                        "    cancelable:true" +
                        "  }));" +
                        "}",
                null
        );
    }

    public void fillAccountNumberField() {
        String number = "1548994846";
        String js = "document.querySelector('input.account-number').value = '" + number + "';";
        webView.evaluateJavascript(js, null);
    }

    private void fillSelect2Search() {
        webView.evaluateJavascript(
                "var input = document.querySelector('input.select2-search__field');" +
                        "if (input) {" +
                        "  var r = input.getBoundingClientRect();" +
                        "  var inView = (r.top >= 0 && r.left >= 0 && " +
                        "                r.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "                r.right <= (window.innerWidth || document.documentElement.clientWidth));" +
                        "  if (inView) {" +
                        "    input.value = 'first bank';" +
                        "    input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "  }" +
                        "}",
                null
        );
    }

    public void clickExtractAccountNumberButton() {
        webView.evaluateJavascript(
                "document.querySelector('button.extract-account-number')?.click();",
                null
        );
    }

    public void toastAllTableFields() {
        webView.evaluateJavascript(
                "(function() {" +
                        "  var rows = document.querySelectorAll('table.nuban tr');" +
                        "  for (var i = 0; i < rows.length; i++) {" +
                        "    var th = rows[i].querySelector('th');" +
                        "    var td = rows[i].querySelector('td');" +
                        "    if (th && td && th.textContent.trim() === 'Account Name') {" +
                        "      return td.textContent.trim();" +
                        "    }" +
                        "  }" +
                        "  return null;" +
                        "})()",
                result -> {
                    if (result != null && !result.equals("null")) {
                        String clean = result.replaceAll("^\"|\"$", "").replace("\\", "");
                        Toast.makeText(webView.getContext(), clean, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(webView.getContext(), "Account Name not found", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void runSelect2Sequence() {
        handler.postDelayed(() -> {
            fillAccountNumberField();

            handler.postDelayed(() -> {
                clickSelect2Dropdown();

                handler.postDelayed(() -> {
                    //fillSelect2Search();

                    handler.postDelayed(() -> {
                        clickExtractAccountNumberButton();

                        handler.postDelayed(this::toastAllTableFields, 1000);

                    }, 500);

                }, 500);

            }, 500);
        }, 500);
    }
}

