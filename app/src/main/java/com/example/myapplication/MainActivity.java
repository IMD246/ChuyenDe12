package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.source.BlogAdapter;
import com.example.myapplication.source.Blog;
import com.example.myapplication.source.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView_blog;
    RecyclerView recyclerView_menu;
    ArrayList<Blog> listBlog;
    ArrayList<Blog> newListBlog;
    ArrayList<String> listMenu;
    BlogAdapter blogAdapter ;

    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setAdapterForMenu();
        setAdapter();
    }

    private void setAdapterForMenu() {
        listMenu = new ArrayList<>();
        listMenu.add("mostFavorite");
        listMenu.add("new");
        listMenu.add("yourFavorite");
        menuAdapter = new MenuAdapter(MainActivity.this,listMenu,recyclerView_menu);
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this,3);
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
        recyclerView_blog = findViewById(R.id.rv_blog);
        recyclerView_menu = findViewById(R.id.rv_menu);
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
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        BlogAdapter blogAdapter = new BlogAdapter(MainActivity.this);
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
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        BlogAdapter blogAdapter = new BlogAdapter(MainActivity.this);
        blogAdapter.setListBlog(temp);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_blog.setAdapter(blogAdapter);
        recyclerView_blog.setLayoutManager(manager);
        blogAdapter.notifyDataSetChanged();
    }
}