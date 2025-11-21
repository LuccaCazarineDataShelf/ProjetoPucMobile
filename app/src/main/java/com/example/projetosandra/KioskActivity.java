package com.example.projetosandra;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KioskActivity extends AppCompatActivity
        implements MenuListFragment.MenuReceiver, CartFragment.CartHost {

    private FirebaseFirestore db;
    private static final String WAITER_ID = "w1";
    private static final String TABLE_ID  = "mesa-3";
    private final List<CartLine> cart = new ArrayList<>();
    private List<String> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk);

        db = FirebaseFirestore.getInstance();

        categories = DataProvider.categories();
        if (!categories.contains("Carrinho")) categories.add("Carrinho");

        TabLayout tabs = findViewById(R.id.tabLayout);
        ViewPager2 pager = findViewById(R.id.viewPager);
        pager.setAdapter(new CategoryPagerAdapter(this, categories));
        new TabLayoutMediator(tabs, pager, (tab, pos) -> tab.setText(categories.get(pos))).attach();

        TextView txtStatus = findViewById(R.id.txtStatus);

        Button markReady = findViewById(R.id.btnMarkReadyDemo);
        markReady.setOnClickListener(v -> demoMarkAnyOrderReady(TABLE_ID));

        Button btnCall = findViewById(R.id.btnCallWaiter);
        btnCall.setOnClickListener(v -> {
            txtStatus.setText("Atendimento solicitado, aguarde o garçom");
            callWaiter(TABLE_ID);
            txtStatus.postDelayed(() -> txtStatus.setText(""), 5000);
        });

        FloatingActionButton fab = findViewById(R.id.fabCallWaiter);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                txtStatus.setText("Atendimento solicitado, aguarde o garçom");
                callWaiter(TABLE_ID);
                txtStatus.postDelayed(() -> txtStatus.setText(""), 5000);
            });
        }
    }
    @Override public void onAddItem(MenuItemModel item) {
        int idx = indexOf(item);
        if (idx >= 0) {
            cart.get(idx).qty += 1;
        } else {
            cart.add(new CartLine(item, 1));
        }
        Snackbar.make(findViewById(android.R.id.content),
                item.name + " adicionada(o)!", Snackbar.LENGTH_SHORT).show();
    }

    private int indexOf(MenuItemModel item) {
        for (int i = 0; i < cart.size(); i++) {
            MenuItemModel it = cart.get(i).item;
            if (it.name.equals(item.name) && it.category.equals(item.category)) {
                return i;
            }
        }
        return -1;
    }

    // ===== CartHost (usado pela aba Carrinho) =====
    @Override public List<CartLine> cartLines() { return cart; }
    @Override public void incAt(int position) { cart.get(position).qty += 1; }
    @Override public void decAt(int position) {
        CartLine line = cart.get(position);
        line.qty -= 1;
        if (line.qty <= 0) cart.remove(position);
    }
    @Override public void clearCart() { cart.clear(); }

    @Override public void confirmOrderFromCart() {
        if (cart.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Carrinho vazio", Snackbar.LENGTH_SHORT).show();
            return;
        }
        sendOrder(TABLE_ID, new ArrayList<>(cart)); // envia com qty
    }

    //Chama o garçom
    private void callWaiter(String tableId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tableId", tableId);
        payload.put("reason", "WAITER");
        payload.put("status", "OPEN");
        payload.put("assignedWaiterId", WAITER_ID);
        payload.put("createdAt", FieldValue.serverTimestamp());

        db.collection("calls").add(payload);
    }

    // Envia o pedido com quantidades corretas
    private void sendOrder(String tableId, List<CartLine> lines) {
        Map<String, Object> order = new HashMap<>();
        order.put("tableId", tableId);
        order.put("status", "NEW");
        order.put("assignedWaiterId", WAITER_ID);
        order.put("createdAt", FieldValue.serverTimestamp());

        ArrayList<Map<String, Object>> items = new ArrayList<>();
        for (CartLine l : lines) {
            Map<String, Object> mi = new HashMap<>();
            mi.put("name", l.item.name);
            mi.put("category", l.item.category);
            mi.put("price", l.item.price);
            mi.put("qty", l.qty);
            items.add(mi);
        }
        order.put("items", items);

        db.collection("orders").add(order)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(this, "Pedido enviado!", Toast.LENGTH_SHORT).show();
                    cart.clear();
                    // Volta para a primeira aba após confirmar
                    TabLayout tabs = findViewById(R.id.tabLayout);
                    tabs.getTabAt(0).select();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Falha ao enviar pedido", Toast.LENGTH_SHORT).show());
    }

    // Marca o pedido NEW mais recente como READY (simula cozinha)
    private void demoMarkAnyOrderReady(String tableId) {
        db.collection("orders")
                .whereEqualTo("tableId", tableId)
                .whereEqualTo("status", "NEW")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(qs -> {
                    if (!qs.isEmpty()) {
                        String id = qs.getDocuments().get(0).getId();
                        db.collection("orders").document(id)
                                .update("status", "READY")
                                .addOnSuccessListener(v ->
                                        Toast.makeText(this, "Pedido PRONTO (demo)", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Falha ao marcar pronto", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Nenhum pedido NEW para marcar PRONTO", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
