package csc248.smirn42.collegesearch2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> implements Filterable {

    private List<CollegeItem> collegeList;
    private List<CollegeItem> collegeListFull;
    private ItemClickListener itemClickListener;
    private Context context;
    private DatabaseCollege collegeDB;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String satRead, satMath, actEng, actMath, cName, cId, webString, cCity, cState, cZip, cCostIn, cCostOut, cSatRead25, cSatRead75, cSatMath25, cSatMath75, cActEng25, cActEng75, cActMath25, cActMath75;
    private Button web;
    private String satChance, actChance;

    public CustomRecyclerAdapter(List<CollegeItem> collegeList, Context context) {
        this.collegeList = collegeList;
        this.context = context;
        collegeListFull = new ArrayList<>(collegeList);
    }

    @NonNull
    @Override
    public CustomRecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.college_item, parent, false);
        return new CustomRecyclerAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomRecyclerAdapter.CustomViewHolder holder, int position) {

        holder.name.setText(collegeList.get(position).getName());
        holder.id.setText(collegeList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return collegeList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return collegeFilter;
    }

    public Filter getFilterName() {
        return collegeFilterName;
    }

    private Filter collegeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CollegeItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(collegeListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(CollegeItem college : collegeListFull) {
                    if(college.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(college);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            collegeList.clear();
            collegeList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private Filter collegeFilterName = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CollegeItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(collegeListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(CollegeItem college : collegeListFull) {
                    if(college.getId().toLowerCase().contains(filterPattern)) {
                        filteredList.add(college);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            collegeList.clear();
            collegeList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, id;

        public CustomViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.college_name);
            id = v.findViewById(R.id.college_id);
            Button details = v.findViewById(R.id.btn_college_details);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetails(name.getText().toString());
                    getScores(UserPage.username);
                    acceptanceChance();
                    if(cCostIn.matches("null")) {
                        cCostIn = "no data";
                    } else {
                        cCostIn = "$ " + cCostIn;
                    }
                    if(cCostOut.matches("null")) {
                        cCostOut = "no data";
                    } else {
                        cCostOut = "$ " + cCostOut;
                    }
                    showDialog();

                }
            });
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public void getDetails(String details) {
        collegeDB = new DatabaseCollege(context);
        sqLiteDatabase = collegeDB.getReadableDatabase();
        Cursor cursor = collegeDB.getCollegeDetails(details);
        if(cursor.moveToFirst()) {
            cName = cursor.getString(0);
            cId = cursor.getString(1);
            cCity = cursor.getString(2);
            cState = cursor.getString(3);
            cZip = cursor.getString(4);
            webString = cursor.getString(5);
            cCostIn = cursor.getString(6);
            cCostOut = cursor.getString(7);
            cSatRead25 = cursor.getString(8);
            cSatRead75 = cursor.getString(9);
            cSatMath25 = cursor.getString(10);
            cSatMath75 = cursor.getString(11);
            cActEng25 = cursor.getString(12);
            cActEng75 = cursor.getString(13);
            cActMath25 = cursor.getString(14);
            cActMath75 = cursor.getString(15);
            cursor.close();
        } else {
            Log.d("error ", "something went wrong");
        }
    }

    public void getScores(String user) {
        dbHelper = new DatabaseHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getScores(user, sqLiteDatabase);
        if(cursor.moveToFirst()) {
            satRead = cursor.getString(0);
            satMath = cursor.getString(1);
            actEng = cursor.getString(2);
            actMath = cursor.getString(3);
        }
    }

    public void acceptanceChance() {
        //SAT chance
        if(satRead.matches("0") || satMath.matches("0")) {
            satChance = "no user data";
        } else {
            int userScoreSatRead = Integer.parseInt(satRead);
            int userScoreSatMath = Integer.parseInt(satMath);
            int userSat = (userScoreSatRead + userScoreSatMath)/2;

            if(cSatRead25.matches("null") || cSatRead75.matches("null") || cSatMath25.matches("null") || cSatMath75.matches("null")) {
                satChance = "no college data";
            } else {
                double cSatRead25score = Double.parseDouble(cSatRead25);
                double cSatRead75score = Double.parseDouble(cSatRead75);
                double cSatMath25score = Double.parseDouble(cSatMath25);
                double cSatMath75score = Double.parseDouble(cSatMath75);

                double sat25 = (cSatRead25score + cSatMath25score)/2;
                double sat75 = (cSatRead75score + cSatMath75score)/2;

                if(userSat < sat25) {
                    satChance = "poor";
                } else if(userSat > sat25 && userSat < sat75) {
                    satChance = "good";
                } else {
                    satChance = "excellent";
                }
            }
        }
        //ACT chance
        if(actEng.matches("0") || actMath.matches("0")) {
            actChance = "no user data";
        } else {
            double userScoreActEng = Double.parseDouble(actEng);
            double userScoreActMath = Double.parseDouble(actMath);
            double userAct = (userScoreActEng + userScoreActMath)/2;

            if(cActEng25.matches("null") || cActEng75.matches("null") || cActMath25.matches("null") || cActMath75.matches("null")) {
                actChance = "no college data";
            } else {
                double cActEng25Score = Double.parseDouble(cActEng25);
                double cActEng75Score = Double.parseDouble(cActEng75);
                double cActMath25Score = Double.parseDouble(cActMath25);
                double cActMath75Score = Double.parseDouble(cActMath75);

                double act25 = (cActEng25Score + cActMath25Score)/2;
                double act75 = (cActEng75Score + cActMath75Score)/2;

                if(userAct < act25) {
                    actChance = "poor";
                } else if(userAct > act25 && userAct < act75) {
                    actChance = "good";
                } else {
                    actChance = "excellent";
                }
            }
        }
    }

    public void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.details_dialog, null);

        TextView nameView = v.findViewById(R.id.details_name);
        TextView idView = v.findViewById(R.id.details_id);
        TextView addressView = v.findViewById(R.id.details_address);
        TextView costInView = v.findViewById(R.id.details_cost_in);
        TextView costOutView = v.findViewById(R.id.details_cost_out);
        TextView chanceSatView = v.findViewById(R.id.details_chance_sat);
        TextView chanceActView = v.findViewById(R.id.details_chance_act);
        web = v.findViewById(R.id.btn_website);
        final Context context = this.context;
        if(webString.matches("null")) {
            web.setVisibility(View.GONE);
        } else {
            web.setVisibility(View.VISIBLE);
            if (!webString.startsWith("http")) {
                webString = "http://" + webString;
            }
            web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(webString);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }
        
        nameView.setText(cName);
        idView.setText("ID: " + cId);
        addressView.setText("location: " + cCity + " " + cState + " " + cZip);
        costInView.setText("Cost in-state: " + cCostIn);
        costOutView.setText("Cost out-of-state: " + cCostOut);
        chanceSatView.setText("My chances (SAT): " + satChance);
        chanceActView.setText("My chances (ACT): " + actChance);

        AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.show();
    }
}