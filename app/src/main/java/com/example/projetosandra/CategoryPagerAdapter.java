package com.example.projetosandra;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class CategoryPagerAdapter extends FragmentStateAdapter {

    private final List<String> categories;

    public CategoryPagerAdapter(@NonNull FragmentActivity fa, List<String> categories) {
        super(fa);
        this.categories = categories;
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        String cat = categories.get(position);
        if ("Carrinho".equals(cat)) return new CartFragment();
        return MenuListFragment.newInstance(cat);
    }

    @Override
    public int getItemCount() { return categories.size(); }
}
