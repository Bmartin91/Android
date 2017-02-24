package com.valenciaprogrammers.dolphintracking;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Brannon on 2/18/2017.
 */

public class CSVWriter extends Activity
{
    Context context; //  Needs the context from the MainActivity
    private String fileName = "DolphinCSV.csv";
    private Cursor curs = null;

    public CSVWriter(Context context)
    {
        this.context = context;
    }
	
	public void WriteCSVFile()
    {
        File file = GetCSVFile();
        String csvInfo = CreateCSVString();

        try
        {
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter os = new OutputStreamWriter(fos);
            os.write(csvInfo);
            os.close();

            Toast.makeText(context, "CSV file has been created in Documents", Toast.LENGTH_LONG).show();
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String CreateCSVString()
    {
        String result = "";
        Cursor cur = GetDatabaseInfo();
        String[] columns = cur.getColumnNames();
        boolean dt = false;

        //  Split each DateTime column into separate date and time columns
        for(String s : columns)
        {
            switch (s)
            {
                case DolphinContract.DolphinTable.ENTERED_DATE_TIME:
                    result += "Entered_Date,Entered_Time,";
                    break;

                case DolphinContract.DolphinTable.OBSERVED_DATE_TIME:
                    result += "Observed_Date,Observed_Time,";
                    break;

                case DolphinContract.ObservationTable.START_DATE_TIME:
                    result += "Start_Date,Start_Time,";
                    break;

                case DolphinContract.ObservationTable.END_DATE_TIME:
                    result += "End_Date,End_Time,";
                    break;

                default:
                    result += s + ",";
            }
        }

        result += "\n";  //  Add a line after the column headers

        while (cur.moveToNext())
        {
            int count  = cur.getColumnCount();

            //  Take each DateTime string and split it
            for(int i = 0; i < count; i++)
            {
                switch (i)
                {
                    case 3:  //  This is Entered_DateTime
                        result += Time.getDateFromDateTime(cur.getString(i)) + ",";
                        result += Time.getTimeFromDateTime(cur.getString(i)) + ",";
                    break;

                    case 4:  //  This is Observed_DateTime
                        result += Time.getDateFromDateTime(cur.getString(i)) + ",";
                        result += Time.getTimeFromDateTime(cur.getString(i)) + ",";
                        break;

                    case 23:  //  This is Start_DateTime
                        result += Time.getDateFromDateTime(cur.getString(i)) + ",";
                        result += Time.getTimeFromDateTime(cur.getString(i)) + ",";
                        break;

                    case 24:  //  This is End_DateTime
                        result += Time.getDateFromDateTime(cur.getString(i)) + ",";
                        result += Time.getTimeFromDateTime(cur.getString(i)) + ",";
                        break;

                    default: result += cur.getString(i) + ",";  //  All columns that are not date/time
                    break;
                }
            }
            result += "\n";
        }
        cur.close();
        return result;
    }

    private Cursor GetDatabaseInfo()
    {
        //  Gets all the info out of the database
        ADDatabaseHelper help = new ADDatabaseHelper(context);
        return help.readAllDataFromDB();
    }

    private File GetCSVFile()
    {
        //  Gets the file that will be the CSV File and returns it
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        return file;
    }
}
