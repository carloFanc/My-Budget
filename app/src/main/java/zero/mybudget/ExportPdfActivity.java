package zero.mybudget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExportPdfActivity extends Activity {

    private Button btnFrom,btnTo,btnExport;
    private TextView Total;
    private int mYear,mMonth,mDay;
    private DBManager dbManager;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private String[] objPos,objNeg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Export Pdf");

        setContentView(R.layout.layout_data_range);
        btnFrom = (Button) findViewById(R.id.btn_date_from);
        btnTo = (Button) findViewById(R.id.btn_date_to);
        Total = (TextView) findViewById(R.id.tv_total);
        btnExport = (Button) findViewById(R.id.search_history);

        DateFormat df1=new SimpleDateFormat("dd-MM-yyyy");
        String datef= df1.format(java.util.Calendar.getInstance().getTime());
        btnFrom.setText(datef);
        btnTo.setText(datef);
        Total.setText("");
        btnExport.setText("EXPORT");

        btnFrom.setOnClickListener(new View.OnClickListener()
        {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ExportPdfActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(c.getTime());
                                btnFrom.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener()
        {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ExportPdfActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String cdate = formatter.format(c.getTime());
                                btnTo.setText(cdate);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        dbManager = new DBManager(this);
        dbManager.open();
        btnExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String fromZ, toZ;
                fromZ = btnFrom.getText().toString();
                toZ = btnTo.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date dateFrom = sdf.parse(fromZ);
                    Date dateTo = sdf.parse(toZ);
                    if(dateFrom.after(dateTo)  ){
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.history_range), "Incorrect Dates!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{

                        try {
                            createPdfWrapper(fromZ,toZ);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (java.text.ParseException e){
                    e.printStackTrace();
                }
            }
            });


    }
    private void createPdfWrapper(String from,String to) throws FileNotFoundException,DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf(from,to);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        final String fromZ, toZ;
                        fromZ = btnFrom.getText().toString();
                        toZ = btnTo.getText().toString();
                        createPdfWrapper(fromZ,toZ);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void createPdf(String from,String to ) throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"ExportPortfolio.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        String result="";
        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLDITALIC);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.NORMAL);
        Chunk chunk = new Chunk("Export", chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 1);
        chapter.setNumberDepth(0);
        chapter.add(new Paragraph("Income", paragraphFont));
        document.add(chapter);
        objPos = dbManager.exportRecordPdf(from,to,"positive");
        if(objPos.length != 0) {
            for(int i=0;i<objPos.length;i++){
                document.add(new Paragraph(objPos[i]));
            }
        }
        Chunk chunk2 = new Chunk("Export", chapterFont);
        Chapter chapter2 = new Chapter(new Paragraph(chunk2), 1);
        chapter2.setNumberDepth(0);
        chapter2.add(new Paragraph("Outcome", paragraphFont));
        document.add(chapter2);
        objNeg = dbManager.exportRecordPdf(from,to,"negative");
        if(objNeg.length != 0) {
            for(int i=0;i<objNeg.length;i++){
                document.add(new Paragraph(objNeg[i]));
            }
        }
        document.close();
        previewPdf();

    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }
}
