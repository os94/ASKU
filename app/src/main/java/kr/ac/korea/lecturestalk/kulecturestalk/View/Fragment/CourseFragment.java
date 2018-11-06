package kr.ac.korea.lecturestalk.kulecturestalk.View.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ac.korea.lecturestalk.kulecturestalk.Adapter.PostListAdapter;
import kr.ac.korea.lecturestalk.kulecturestalk.Model.Post;
import kr.ac.korea.lecturestalk.kulecturestalk.R;
import kr.ac.korea.lecturestalk.kulecturestalk.View.EmptyRecyclerView;

public class CourseFragment extends Fragment {
    private TextView mAllTab;
    private TextView mNoticeTab;
    private TextView mMaterialsTab;
    private TextView mQnaTab;
    private TextView mEtcTab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        mAllTab = view.findViewById(R.id.course_tab_all);
        mNoticeTab = view.findViewById(R.id.course_tab_notice);
        mMaterialsTab = view.findViewById(R.id.course_tab_materials);
        mQnaTab = view.findViewById(R.id.course_tab_qna);
        mEtcTab = view.findViewById(R.id.course_tab_etc);

        mAllTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(getContext(), "All Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mNoticeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(getContext(), "Notice Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mMaterialsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(getContext(), "Materials Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mQnaTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                mEtcTab.setTextColor(getResources().getColor(android.R.color.black, null));
                Toast.makeText(getContext(), "Q&A Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mEtcTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mNoticeTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mMaterialsTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mQnaTab.setTextColor(getResources().getColor(android.R.color.black, null));
                mEtcTab.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                Toast.makeText(getContext(), "Etc Tab Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Initialize with 'All' tab clicked

        // Use EmptyRecyclerView
        EmptyRecyclerView recyclerView = view.findViewById(R.id.course_posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Fetch the empty view from the layout and set it on the new recycler view
        View emptyView = view.findViewById(R.id.course_post_empty);
        recyclerView.setEmptyView(emptyView);

        List<Post> posts = new ArrayList<>();
        // TODO: Fetch list of posts from the database

        // Test Data
//        Post p = new Post(1, "juhan", "Software Engineering", "2018-2",
//                "Hoh", "월수5", "공지", "Fuck Fuck Fuck",
//                "holy shit somebody help me...", new ArrayList<Integer>(),
//                new ArrayList<String>(), 7, System.currentTimeMillis(), 0, null);
//        posts.add(p);

        PostListAdapter adapter = new PostListAdapter(posts);
        recyclerView.setAdapter(adapter);


        return view;
    }
}
