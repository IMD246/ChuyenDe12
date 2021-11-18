package com.example.EnglishBeginner.Blog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EnglishBeginner.Adapter.BlogAdapter;
import com.example.EnglishBeginner.Adapter.MenuAdapter;
import com.example.EnglishBeginner.DTO.Blog;
import com.example.EnglishBeginner.R;

import java.util.ArrayList;

public class BlogFragment extends Fragment {
    private View view;
    RecyclerView recyclerView_blog;
    RecyclerView recyclerView_menu;
    ArrayList<Blog> listBlog;
    ArrayList<Blog> newListBlog;
    ArrayList<String> listMenu;
    private MenuAdapter menuAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blog, container, false);
        setControl();
        setAdapterForMenu();
        setAdapter();
        return view;
    }

    private void setAdapterForMenu() {
        listMenu = new ArrayList<>();
        listMenu.add("mostFavorite");
        listMenu.add("new");
        listMenu.add("yourFavorite");
        menuAdapter = new MenuAdapter(getContext(),listMenu,recyclerView_menu);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView_menu.setAdapter(menuAdapter);
        recyclerView_menu.setLayoutManager(manager);
        menuAdapter.setSetAdapterOfListBlog(new MenuAdapter.setAdapterCallBack() {
            @Override
            public void setAdapter(String typeBlog) {
                setDataFollowOption(typeBlog);
            }
        });
    }

    private void setDataFollowOption(String typeBlog) {
        ArrayList<Blog> arrayList = new ArrayList<>();
        Log.d("test", "setDataFollowOption: "+listBlog.size());

        for (Blog blog : listBlog)
        {
            if (blog.getTypeMenu().equals(typeBlog))
            {
                arrayList.add(blog);
            }
        }
        Log.d("test2", "setDataFollowOption: "+arrayList.size());

        setAdapterc2(arrayList);
    }

    private void setControl() {
        newListBlog = new ArrayList<>();
        listBlog = new ArrayList<>();
        Blog blog1 = new Blog("1","19/7/2121 10:pm","how to sell 100000 pack of notthing,","",120,41,203,"mostFavorite");
        Blog blog2 = new Blog("1","19/7/2121 10:pm","i am yasuo 0/10,","",102,43,220,"yourFavorite");
        Blog blog3 = new Blog("1","19/7/2121 10:pm","how to sell 100000 pack of notthing,w;elfja;rjpaowt;ljsdetpa2u3-4ojqpfhvxc.b,xlfk;EKht.eznb;fcxf;ew,5ntyw 4ht","",510,24,260,"new");
        listBlog.add(blog1);
        listBlog.add(blog2);
        listBlog.add(blog2);
        listBlog.add(blog3);
        listBlog.add(blog3);
        listBlog.add(blog1);
        recyclerView_blog = view.findViewById(R.id.rv_blog);
        recyclerView_menu = view.findViewById(R.id.rv_menu);
    }
    private void setAdapter() {
        Log.d("TAG", "setAdapter: "+listBlog.size());
        for (Blog blog : listBlog)
        {
            if (blog.getTypeMenu().equals("mostFavorite"))
            {
                newListBlog.add(blog);
            }
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        BlogAdapter blogAdapter = new BlogAdapter(getContext());
        blogAdapter.setListBlog(newListBlog);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_blog.setAdapter(blogAdapter);
        recyclerView_blog.setLayoutManager(manager);
        blogAdapter.notifyDataSetChanged();
    }
    private void setAdapterc2(ArrayList<Blog> temp) {
        Log.d("TAG", "setAdapter: "+listBlog.size());
        for (Blog blog : listBlog)
        {
            if (blog.getTypeMenu().equals("mostFavorite"))
            {
                newListBlog.add(blog);
            }
        }
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        BlogAdapter blogAdapter = new BlogAdapter(getContext());
        blogAdapter.setListBlog(temp);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_blog.setAdapter(blogAdapter);
        recyclerView_blog.setLayoutManager(manager);
        blogAdapter.notifyDataSetChanged();
    }
}