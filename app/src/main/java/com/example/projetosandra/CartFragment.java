package com.example.projetosandra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class CartFragment extends Fragment {

    public interface CartHost {
        List<CartLine> cartLines();
        void confirmOrderFromCart();
        void clearCart();
        void incAt(int position);
        void decAt(int position); // remove quando chegar a 0
    }

    private RecyclerView recycler;
    private TextView txtTotal;
    private CartAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        recycler = v.findViewById(R.id.recyclerCart);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        txtTotal = v.findViewById(R.id.txtTotal);
        Button btnClear = v.findViewById(R.id.btnClear);
        Button btnConfirm = v.findViewById(R.id.btnConfirm);

        if (getActivity() instanceof CartHost) {
            CartHost host = (CartHost) getActivity();

            adapter = new CartAdapter(host.cartLines(), new CartAdapter.Listener() {
                @Override public void onInc(int position) {
                    host.incAt(position);
                    adapter.notifyItemChanged(position);
                    updateTotal(host.cartLines());
                }
                @Override public void onDec(int position) {
                    int before = host.cartLines().size();
                    host.decAt(position);
                    int after = host.cartLines().size();
                    if (after < before) adapter.notifyItemRemoved(position);
                    else adapter.notifyItemChanged(position);
                    updateTotal(host.cartLines());
                }
            });
            recycler.setAdapter(adapter);

            updateTotal(host.cartLines());

            btnClear.setOnClickListener(_v -> {
                host.clearCart();
                adapter.notifyDataSetChanged();
                updateTotal(host.cartLines());
            });

            btnConfirm.setOnClickListener(_v -> {
                if (host.cartLines().isEmpty()) {
                    Snackbar.make(v, "Carrinho vazio", Snackbar.LENGTH_SHORT).show();
                } else {
                    host.confirmOrderFromCart();
                    adapter.notifyDataSetChanged();
                    updateTotal(host.cartLines());
                }
            });
        }
        return v;
    }

    private void updateTotal(List<CartLine> lines) {
        double sum = 0.0;
        for (CartLine l : lines) sum += l.subtotal();
        txtTotal.setText("Total: R$ " + String.format("%.2f", sum));
    }
}
