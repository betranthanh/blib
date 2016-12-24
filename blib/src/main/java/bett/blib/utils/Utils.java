package bett.blib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bett on 7/29/16.
 */
public class Utils {

    private static Calendar dateTime = Calendar.getInstance();

    public static int calculateInSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void gotoActivity(Activity activity, Class target, Bundle mBundle, boolean isFinish) {
        Intent intent = new Intent(activity, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void gotoActivity(Activity activity, Class target, boolean isFinish) {
        Intent intent = new Intent(activity, target);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    /**
     * Returns the currently available memory (ram) in bytes.
     * @param _context The context.
     * @return The available memory in bytes.
     */
    public static long getAvailableMemory(Context _context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) _context.getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem;
        return availableMegs;
    }

    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * setContentView(R.layout.main_activity);
     * Utils.setupKeyboardForAllControl(this, findViewById(R.id.rootView));
     * @param activity
     * @param view
     */
    public static void setupKeyboardForAllControl(final Activity activity, View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });

        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupKeyboardForAllControl(activity, innerView);
            }
        }
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * So sanh 2 version build
     * @param oldVersionName
     * @param newVersionName
     * @return
     */
    public static int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i ++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length)?1:-1;
        }

        return res;
    }

    public static void appendLog(Exception ex) {
        appendLog(ex.getCause() + " " + ex.getMessage());
    }

    public static void appendLog(String text)
    {
        boolean isWrite = true;

        SimpleDateFormat dateFormatterToServer = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if (isWrite) {
            Date currentDate = new Date();
            String fileName = "log-" + dateFormatterToServer.format(currentDate) + ".txt";
            File folder = new File("sdcard/BeatMe");
            if (!folder.exists()) {
                folder.mkdir();
            }

            File logFile = new File(folder.getAbsolutePath() + File.separator + fileName);

            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                SimpleDateFormat serverDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                buf.append(serverDateTimeFormat.format(currentDate) + ": " + text);
                buf.newLine();
                buf.close();
                Logger.d(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static LocationManager locationManager;
    static Location location;
    static double latitude, longitude;
    static boolean isNetworkEnabled = false;
    static boolean isGPSEnabled = false;
    /**
     * using for getting your current location
     *
     * @param context
     * @return current location
     */
    @SuppressWarnings("static-access")
    public static Location getCurrentLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
//                Common.showGPSDisabledAlert("Please enable your location or connect to cellular network.", context);
            } else {
                if (isNetworkEnabled) {
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * use to show address location pin on map.
     *
     * @param mContext
     * @param address  to show on map.
     */
    public static void showAddressOnMap(Context mContext, String address) {
        address = address.replace(' ', '+');
        Intent geoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + address));
        mContext.startActivity(geoIntent);
    }

    /**
     * use to show datepicker
     *
     * @param mContext
     * @param format    of the date format
     * @param mTextView in which you have to set selected date
     */
    public static void showDatePickerDialog(final Context mContext,
                                            final String format, final TextView mTextView) {
        new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
                dateTime.set(year, monthOfYear, dayOfMonth);

                mTextView.setText(dateFormatter.format(dateTime.getTime())
                        .toString());
            }
        }, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * show timepicker
     *
     * @param mContext
     * @param mTextView formar of the time
     * @return show timepicker
     */
    public static void showTimePickerDialog(final Context mContext,
                                            final TextView mTextView) {
        new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateTime.set(Calendar.MINUTE, minute);

                mTextView.setText(timeFormatter.format(dateTime.getTime())
                        .toString());
            }
        }, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE),
                false).show();
    }

    public static boolean isSDCardAvailable(Context mContext) {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    public static boolean isWebsiteUrlValid(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getDayFormatFromMillisecond(long milliseconds){
        if (milliseconds <= 0) {
            return "00-00-00";
        }
        StringBuffer text = new StringBuffer("");
        if (milliseconds > HOUR) {
            if (milliseconds / HOUR >= 10) {
                text.append(milliseconds / HOUR);
            } else {
                text.append("0").append(milliseconds / HOUR);
            }
            milliseconds %= HOUR;
        } else {
            text.append("00");
        }
        text.append("-");

        if (milliseconds > MINUTE) {
            if (milliseconds / MINUTE >= 10) {
                text.append(milliseconds / MINUTE);
            } else {
                text.append("0").append(milliseconds / MINUTE);
            }
            milliseconds %= MINUTE;
        } else {
            text.append("00");
        }
        text.append("-");

        if (milliseconds > SECOND) {
            if (milliseconds / SECOND >= 10) {
                text.append(milliseconds / SECOND);
            } else {
                text.append("0").append(milliseconds / SECOND);
            }
//            milliseconds %= SECOND;
        } else {
            text.append("00");
        }
        return text.toString();
    }

    public static void loadImageFrescoFromInternet(SimpleDraweeView image, String imageUrl) {
        if (StringUtils.isNotEmpty(imageUrl)) {
            Uri imgUri = Uri.parse(imageUrl);
            image.setImageURI(imgUri);
        } else {
            image.requestLayout();
        }

    }

    public static void loadImageFrescoFromDrawable(SimpleDraweeView image, int drawableImage) {
        Uri imgUri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(drawableImage))
                .build();
        image.setImageURI(imgUri);
    }

    public static void loadImageFrescoFromLocalFile(SimpleDraweeView image, String filePath) {
        Uri imgUri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(filePath)
                .build();
        image.setImageURI(imgUri);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float dpToPx(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float pxToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static File takeScreenshot(Activity mActivity, View v1) {
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/PICTURES/Screenshots/" + System.currentTimeMillis() + ".jpg";

            // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            MediaScannerConnection.scanFile(mActivity,
                    new String[]{imageFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            return imageFile;
        } catch (Exception ex) {
            Logger.e(ex.getCause() + " " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private static File createImage(Bitmap bmp, String filePath) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(filePath);
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static void setActive(View view) {
        view.setAlpha(1.0f);
    }

    public static void setInActive(View view) {
        view.setAlpha(0.5f);
    }

    public static void loadImage(Context context, final SimpleDraweeView simpleDraweeView, String imageUrl, final int imageHeight) {
        Uri imgUri = null;
        if (StringUtils.isNotEmpty(imageUrl)) {
            imgUri = Uri.parse(imageUrl);
        }
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());

        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();

        ImageRequest requestBuilder = ImageRequestBuilder.newBuilderWithSource(imgUri)
                .setProgressiveRenderingEnabled(true)
                .build();


        ControllerListener<ImageInfo> contollerListener = new BaseControllerListener<ImageInfo>() {

            public void onFinalImageSet(String id, ImageInfo imageinfo, Animatable animatable) {

                if (imageinfo != null) {
                    updateViewSize(imageinfo, simpleDraweeView, imageHeight);
                }
            }
        };
        DraweeController contoller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(contollerListener)
                .setImageRequest(requestBuilder)
                .build();

        simpleDraweeView.setHierarchy(hierarchy);
        simpleDraweeView.setController(contoller);
    }

    private static void updateViewSize(ImageInfo imageinfo, SimpleDraweeView simpleDraweeView, int imageHeight){
        //this is my own implementation of changing simple-drawee-view height
        // you canhave yours using imageinfo.getHeight() or imageinfo.getWidth();
        simpleDraweeView.getLayoutParams().height = imageHeight;

        // don't forget to call this method. thanks to @plamenko for reminding me.
        simpleDraweeView.requestLayout();
    }

    public static int doubleToInt(Double aDouble) {
        return (int) (aDouble + 0.5);
    }

    public static String getColorByColorCode(String color) {
        return "#" + color.replace("#", "");
    }

    public static String saveImageTempByBitmap(Activity activity, Bitmap bmp){
        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        try {
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, StringUtils.generateRandomString() + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush();
            fOut.close();
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(file.getAbsolutePath());
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            activity.sendBroadcast(mediaScanIntent);

            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void gridViewSetting(Context context, GridView gridview, int itemWidth, int seperateWidth) {

        int size = gridview.getAdapter().getCount();
        // Calculated single Item Layout Width for each grid element ....
        Point point = Utils.getScreenSize(context);
//		int width = point.x - point.x / 5;
        int totalWidth = (int) ((itemWidth * size) + (Utils.dpToPx(seperateWidth, context) * Math.max(0, size - 1))) + 1;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(itemWidth);
        gridview.setHorizontalSpacing((int) Utils.dpToPx(seperateWidth, context));
        gridview.setNumColumns(size);
        gridview.requestLayout();
    }

    public static Bitmap getCircleBitmap(Bitmap bm, int size) {

        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, size-4, size-4);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
