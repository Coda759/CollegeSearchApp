package csc248.smirn42.collegesearch2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CollegeFragment extends Fragment implements ItemClickListener{

    private RecyclerView recyclerView;
    private CustomRecyclerAdapter customRecyclerAdapter;
    private List<CollegeItem> collegeItemList = new ArrayList<>();
    private DatabaseCollege dbCollege;
    private EditText cName, cId;
    private Button details;
    TextView collegeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_college, container, false);

        dbCollege = new DatabaseCollege(getActivity());

        recyclerView = myFragmentView.findViewById(R.id.recycler);
        customRecyclerAdapter = new CustomRecyclerAdapter(dbCollege.getCollegeData(), getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customRecyclerAdapter);
        customRecyclerAdapter.setItemClickListener(this);

        cId = myFragmentView.findViewById(R.id.et_ipeds);
        cId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cName.getText().clear();
                CollegeFragment.this.customRecyclerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cName = myFragmentView.findViewById(R.id.et_name);
        cName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cId.getText().clear();
                CollegeFragment.this.customRecyclerAdapter.getFilterName().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        collegeName = myFragmentView.findViewById(R.id.details_name);

        return myFragmentView;

    }

    @Override
    public void onClick(View v, int position) {
        String name = collegeItemList.get(position).getName();
        String id = collegeItemList.get(position).getId();
    }

    public void openWeb(String webAddress) {
        Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(webAddress));
        startActivity(website);
    }
}
