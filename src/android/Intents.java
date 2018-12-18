package cordova.nativeandroid.intents;

        import android.content.Intent;
        import android.util.Log;

        import org.apache.cordova.CallbackContext;
        import org.apache.cordova.CordovaPlugin;
        import org.json.JSONArray;
        import org.json.JSONException;



/**
 * This class echoes a string called from JavaScript.
 */
public class Intents extends CordovaPlugin {

    public CallbackContext savedContext;

//    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//        super.initialize(cordova, webView);
//    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startActivityForResult")) {
            String activity = args.getString(0);
            String b64Image = args.getString(1);
            boolean picker = args.getBoolean(2);
            this.savedContext = callbackContext;
            this.startActivityForResult(activity, b64Image, picker, callbackContext);
            return true;
        }
        return false;
    }

    private void startActivityForResult(String activity, String b64Image, boolean picker, CallbackContext callbackContext) {
        if (activity != null && activity.length() > 0) {
//            callbackContext.success(activity);
            if (this.cordova != null) {

                if((b64Image == null || b64Image.isEmpty() && !picker) || activity == null || activity.isEmpty()){
                    callbackContext.error("'picker' and 'activity' required");
                    return;
                }

                Intent ocrSdkIntent = new Intent();
                ocrSdkIntent.putExtra("picker", picker);

                if(b64Image != null && !b64Image.isEmpty()){
                    ocrSdkIntent.putExtra("b64Image", b64Image);
                }

                ocrSdkIntent.setClassName("docscanner.neoquant.com", activity);
                this.cordova.startActivityForResult((CordovaPlugin) this, ocrSdkIntent, 333);
            }  else {
                callbackContext.error("Cordova not found");
            }

        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("Intent Plugin", "onActivityResult called");
        if(requestCode == 333){
            Log.d("Intent Plugin", intent.getStringExtra("ocrResult"));
            this.savedContext.success(intent.getStringExtra("ocrResult"));
        } else {
            this.savedContext.error(requestCode);
        }
    }
}
