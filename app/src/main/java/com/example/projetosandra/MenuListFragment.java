package com.example.projetosandra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MenuListFragment extends Fragment {

    private static final String ARG_CATEGORY = "cat";

    public static MenuListFragment newInstance(String category) {
        MenuListFragment f = new MenuListFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATEGORY, category);
        f.setArguments(b);
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_list, container, false);

        String cat = getArguments() != null ? getArguments().getString(ARG_CATEGORY, "") : "";
        ArrayList<MenuItemModel> items = DataProvider.byCategory(cat);

        RecyclerView r = v.findViewById(R.id.recycler);
        r.setLayoutManager(new GridLayoutManager(getContext(), 2)); // grade 2 colunas no tablet
        MenuCardAdapter adapter = new MenuCardAdapter(items, item -> {
            if (getActivity() instanceof MenuReceiver) {
                ((MenuReceiver) getActivity()).onAddItem(item);
                Snackbar.make(v, item.name + " adicionada(o)!", Snackbar.LENGTH_SHORT).show();
            }
        });
        r.setAdapter(adapter);
        return v;
    }

    public interface MenuReceiver {
        void onAddItem(MenuItemModel item);
    }
}
