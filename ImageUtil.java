package example.base.util;

import android.content.*;
import android.database.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.provider.*;

import java.io.*;

/**
 * Created by Silver on 9/17/16.
 */

public class ImageUtil {

    private static final int MAX_TRANSFER_SIZE = 500000;

    public static byte[] compressBitmap(File file) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
        bin.mark(bin.available());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bin, null, options);

        int targetHeight = 1200;
        int targetWidth = 1600;
        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);

        if (options.outHeight * options.outWidth >= targetHeight * targetWidth) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / targetHeight
                    : options.outWidth / targetWidth;
            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }

        try {
            bin.reset();
        } catch (IOException e) {
            bin = new BufferedInputStream(new FileInputStream(file));
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(bin, null, options);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        int compressionQuality = 40;
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bs);
        while (bs.toByteArray().length > MAX_TRANSFER_SIZE && compressionQuality >= 10) {
            try {
                bs.flush();
                compressionQuality = (int) (compressionQuality * 0.4);
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bs);
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
        byte[] bytes = bs.toByteArray();
        try {
            bs.close();
            bin.close();
        } catch (IOException e) {
            LogUtil.e(e);
        }
        return bytes;
    }

    public static byte[] rotateImageIfRequired(Context context, Uri uri, byte[] fileBytes,
                                               File theFile) {
        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);
        ByteArrayOutputStream outputStream = null;

        try {

            if (theFile != null) {
                bitmap = rotateImageIfRequired2(bitmap, context, uri, theFile);
            } else {
                bitmap = rotateImageIfRequired(bitmap, context, uri);
            }

            outputStream = new ByteArrayOutputStream();
            int compressionQuality = 40;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, outputStream);

            data = outputStream.toByteArray();
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ignore) {
            }
        }

        return data;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {
        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            LogUtil.w("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    public static Bitmap rotateImageIfRequired2(Bitmap img, Context context, Uri selectedImage,
                                                File theFile) throws IOException {

        if (selectedImage.getPathSegments().get(0).equals("external_files")) {
            ExifInterface ei = new ExifInterface(theFile.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            LogUtil.w("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } else {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        if (img == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        } catch (Exception e) {
            LogUtil.e(e);
        }

        return null;
    }

    public static byte[] compressBitmap(InputStream stream) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(stream);
        bin.mark(bin.available());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bin, null, options);

        int targetHeight = 1200;
        int targetWidth = 1600;
        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);

        if (options.outHeight * options.outWidth >= targetHeight * targetWidth) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / targetHeight
                    : options.outWidth / targetWidth;
            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }

        try {
            bin.reset();
        } catch (IOException e) {
            bin = new BufferedInputStream(stream);
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(bin, null, options);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        int compressionQuality = 40;
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bs);
        while (bs.toByteArray().length > MAX_TRANSFER_SIZE && compressionQuality >= 10) {
            try {
                bs.flush();
                compressionQuality = (int) (compressionQuality * 0.4);
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bs);
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }

        byte[] bytes = bs.toByteArray();
        try {
            bs.close();
            bin.close();
        } catch (IOException e) {
            LogUtil.e(e);
        }
        return bytes;
    }
}
