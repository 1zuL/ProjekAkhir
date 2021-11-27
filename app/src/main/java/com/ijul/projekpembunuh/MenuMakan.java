package com.ijul.projekpembunuh;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.ijul.projekpembunuh.adapter.ItemAdapter;
import com.ijul.projekpembunuh.adapter.OrderAdapter;
import com.ijul.projekpembunuh.model.Solution;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuMakan extends AppCompatActivity implements ItemAdapter.IItemAdapterCallback, OrderAdapter.IOrderAdapterCallback {
    BottomNavigationView bottomNavigation;

    private DrawerLayout drawer;
    private RelativeLayout rlCart;
    private TextView txtCount;
    private TextView Total;
    private RecyclerView rvOrder;
    private TextView txtClearAll;
    private Button btnCompleteOrder;
    private ProgressDialog dialog;
    private com.ijul.projekpembunuh.adapter.OrderAdapter orderAdapter;
    private ArrayList<com.ijul.projekpembunuh.model.Category> categoryList;
    private ArrayList<com.ijul.projekpembunuh.model.SubCategory> subCategoryList;
    private ArrayList<com.ijul.projekpembunuh.model.Item> itemList;
    private ArrayList<com.ijul.projekpembunuh.model.Solution> solutionList;
    private ArrayList<com.ijul.projekpembunuh.model.Order> orderList;

    @Override
    public void onIncreaseDecreaseCallback()
    {
        updateOrderTotal();
        updateBadge();
    }

    @Override
    public void onItemCallback(com.ijul.projekpembunuh.model.Item item)
    {
        dialogItemDetail(item);
    }

    @Override
    public void onAddItemCallback(ImageView imageView, com.ijul.projekpembunuh.model.Item item)
    {
        addItemToCartAnimation(imageView, item, 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_makan);
        List<Solution> solutionList = new ArrayList<>();
        prepareData();

        // Find views
        Total = findViewById(R.id.Total);
        rvOrder = findViewById(R.id.rvOrder);
        txtClearAll = findViewById(R.id.txtClearAll);
        btnCompleteOrder = findViewById(R.id.btnCompleteOrder);

        // set
        rvOrder.setLayoutManager(new LinearLayoutManager(MenuMakan.this));
        orderAdapter = new com.ijul.projekpembunuh.adapter.OrderAdapter(MenuMakan.this, orderList);
        rvOrder.setAdapter(orderAdapter);

        // Get the ViewPager and set it's CategoryPagerAdapter so that it can display items
        ViewPager vpItem = (ViewPager) findViewById(R.id.vpItem);
        com.ijul.projekpembunuh.adapter.CategoryPagerAdapter categoryPagerAdapter = new com.ijul.projekpembunuh.adapter.CategoryPagerAdapter(getSupportFragmentManager(), MenuMakan.this, solutionList);
        vpItem.setOffscreenPageLimit(categoryPagerAdapter.getCount());
        vpItem.setAdapter(categoryPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabCategory = findViewById(R.id.tabCategory);
        tabCategory.setupWithViewPager(vpItem);

        for (int i = 0; i < tabCategory.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabCategory.getTabAt(i);
            tab.setCustomView(categoryPagerAdapter.getTabView(i));
        }

        btnCompleteOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (orderList.size() > 0)
                {
                    dialogCompleteOrder();
                }
            }
        });

        txtClearAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (orderList.size() > 0)
                {
                    dialogClearAll();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.END))
        {
            drawer.closeDrawer(GravityCompat.END);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        final View actionCart = menu.findItem(R.id.actionCart).getActionView();

        txtCount = (TextView) actionCart.findViewById(R.id.txtCount);
        rlCart = (RelativeLayout) actionCart.findViewById(R.id.rlCart);

        rlCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                handleOrderDrawer();
            }
        });

        return true;
    }

    /*
     * Prepares sample data to be used within the application
     */
    private void prepareData()
    {
        categoryList = new ArrayList<com.ijul.projekpembunuh.model.Category>();
        subCategoryList = new ArrayList<com.ijul.projekpembunuh.model.SubCategory>();
        itemList = new ArrayList<com.ijul.projekpembunuh.model.Item>();
        solutionList = new ArrayList<com.ijul.projekpembunuh.model.Solution>();
        orderList = new ArrayList<com.ijul.projekpembunuh.model.Order>();

        categoryList.add(new com.ijul.projekpembunuh.model.Category(1, "All", R.drawable.all));

        categoryList.add(new com.ijul.projekpembunuh.model.Category(2, "Food", R.drawable.food));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(1, 2, "Hamburger"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(2, 2, "Pizza"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(3, 2, "Kebab"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(4, 2, "Dessert"));

        categoryList.add(new com.ijul.projekpembunuh.model.Category(3, "Drinks", R.drawable.drink));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(5, 3, "Water"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(6, 3, "Soda"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(7, 3, "Tea"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(8, 3, "Coffee"));
        subCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(9, 3, "Fruit Juice"));



        //Hamburger
        itemList.add(new com.ijul.projekpembunuh.model.Item(1, 2, 1, "Ultimate Hamburger", 5.0, "https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(2, 2, 1, "Double Cheese Burger", 7.0, "https://1.bp.blogspot.com/-i2e3XPfVwYw/V9GgRgn2Y3I/AAAAAAAAxeM/Ih2LoXrSQr0NBgFKLeupxYNzwGZXBv1VwCLcB/s1600/Hardees-Classic-Double-Cheeseburger.jpg"));

        //Pizza
        itemList.add(new com.ijul.projekpembunuh.model.Item(3, 2, 2, "Pepperoni", 10.99, "https://www.cicis.com/media/1138/pizza_trad_pepperoni.png"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(4, 2, 2, "Mixed Pizza", 11.20, "http://icube.milliyet.com.tr/YeniAnaResim/2016/10/24/tavada-pizza-tarifi-7857137.Jpeg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(5, 2, 2, "Neapolitan Pizza", 12.10, "https://img.grouponcdn.com/deal/2SVzinBnAH17zHrq3HNpurto2gpK/2S-700x420/v1/c700x420.jpg"));

        //Kebab
        itemList.add(new com.ijul.projekpembunuh.model.Item(6, 2, 3, "Garlic-Tomato Kebab", 16.60, "http://www.seriouseats.com/recipes/assets_c/2016/08/20160703-Grilled-Lemon-Garlic-Chicken-Tomato-Kebabs-Basil-Chimichurri-emily-matt-clifton-7-thumb-1500xauto-433447.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(7, 2, 3, "Adana Kebab", 15.0, "http://www.muslumkebap.com/Contents/Upload/MuslumAdana_qyz0b34a.p3w.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(8, 2, 3, "Eggplant Kebab", 18.0, "http://www.muslumkebap.com/Contents/Upload/MuslumPatlicanKebap_y41olk55.kpc.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(9, 2, 3, "Shish Kebab", 10.0, "http://2.bp.blogspot.com/-4_8T6g3qOCU/VVz3oASO5lI/AAAAAAAAUHo/rEw1XlZoxqQ/s1600/Shish.jpg"));

        //Dessert
        itemList.add(new com.ijul.projekpembunuh.model.Item(10, 2, 4, "Pistachio Baklava", 10.0, "http://www.karakoygulluoglu.com/fistikli-kare-baklava-32-15-B.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(11, 2, 4, "Palace Pistachio Baklava", 12.50, "http://www.karakoygulluoglu.com/fistikli-havuc-dilim-baklava-1kg-31-14-B.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(12, 2, 4, "Mixed Baklava in Tray", 60.0, "http://www.karakoygulluoglu.com/karisik-baklava-1-tepsi-66-31-B.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(13, 2, 4, "Tel Kadayıf with Pistachio", 9.0, "http://www.karakoygulluoglu.com/fistikli-tel-kadayif-1-39-44-B.jpg"));

        //Water
        itemList.add(new com.ijul.projekpembunuh.model.Item(14, 3, 5, "0.5 liter", 0.30, "http://cdn.avansas.com/assets/59479/erikli-su-0-5-lt-12-li-1-zoom.jpg"));

        //Fruit Juice
        itemList.add(new com.ijul.projekpembunuh.model.Item(22, 3, 9, "Mixed Fruit Juice", 1.0, "https://images.hepsiburada.net/assets/Taris/500/Taris_4791763.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(23, 3, 9, "Apricot Fruit Juice", 1.0, "http://cdn.avansas.com/assets/53313/cappy-meyve-suyu-kayisi-1-lt-0-zoom.jpg"));
        itemList.add(new com.ijul.projekpembunuh.model.Item(24, 3, 9, "Orange Fruit Juice", 1.0, "https://images.hepsiburada.net/assets/Taris/500/Taris_4791980.jpg"));


        for (com.ijul.projekpembunuh.model.Category categoryItem : categoryList)
        {
            // Temporary list of the current sıb-categories
            ArrayList<com.ijul.projekpembunuh.model.SubCategory> tempSubCategoryList = new ArrayList<>();

            // Temporary list of the current items
            ArrayList<com.ijul.projekpembunuh.model.Item> tempItemList = new ArrayList<>();

            // Temporary map
            Map<com.ijul.projekpembunuh.model.SubCategory, ArrayList<com.ijul.projekpembunuh.model.Item>> itemMap = new HashMap<com.ijul.projekpembunuh.model.SubCategory, ArrayList<com.ijul.projekpembunuh.model.Item>>();

            // categoryId == 1 means category with all items and sub-categories.
            // That's why i add all the sub-categories and items directly.
            if (categoryItem.id == 1)
            {
                itemMap = getItemMap(subCategoryList);

                solutionList.add(new com.ijul.projekpembunuh.model.Solution(categoryItem, subCategoryList, itemList, itemMap));
            }
            else
            {
                tempSubCategoryList = getSubCategoryListByCategoryId(categoryItem.id);
                tempItemList = getItemListByCategoryId(categoryItem.id);
                itemMap = getItemMap(tempSubCategoryList);

                solutionList.add(new com.ijul.projekpembunuh.model.Solution(categoryItem, tempSubCategoryList, tempItemList, itemMap));
            }
        }
    }


    private ArrayList<com.ijul.projekpembunuh.model.SubCategory> getSubCategoryListByCategoryId(int categoryId)
    {
        ArrayList<com.ijul.projekpembunuh.model.SubCategory> tempSubCategoryList = new ArrayList<com.ijul.projekpembunuh.model.SubCategory>();

        for (com.ijul.projekpembunuh.model.SubCategory subCategory : subCategoryList)
        {
            if (subCategory.categoryId == categoryId)
            {
                tempSubCategoryList.add(new com.ijul.projekpembunuh.model.SubCategory(subCategory));
            }
        }

        return tempSubCategoryList;
    }


    private ArrayList<com.ijul.projekpembunuh.model.Item> getItemListByCategoryId(int categoryId)
    {
        ArrayList<com.ijul.projekpembunuh.model.Item> tempItemList = new ArrayList<com.ijul.projekpembunuh.model.Item>();

        for (com.ijul.projekpembunuh.model.Item item : itemList)
        {
            if (item.categoryId == categoryId)
            {
                tempItemList.add(item);
            }
        }

        return tempItemList;
    }

    private ArrayList<com.ijul.projekpembunuh.model.Item> getItemListBySubCategoryId(int subCategoryId)
    {
        ArrayList<com.ijul.projekpembunuh.model.Item> tempItemList = new ArrayList<com.ijul.projekpembunuh.model.Item>();

        for (com.ijul.projekpembunuh.model.Item item : itemList)
        {
            if (item.subCategoryId == subCategoryId)
            {
                tempItemList.add(item);
            }
        }

        return tempItemList;
    }


    private Map<com.ijul.projekpembunuh.model.SubCategory, ArrayList<com.ijul.projekpembunuh.model.Item>> getItemMap(ArrayList<com.ijul.projekpembunuh.model.SubCategory> subCategoryList)
    {
        Map<com.ijul.projekpembunuh.model.SubCategory, ArrayList<com.ijul.projekpembunuh.model.Item>> itemMap = new HashMap<com.ijul.projekpembunuh.model.SubCategory, ArrayList<com.ijul.projekpembunuh.model.Item>>();

        for (com.ijul.projekpembunuh.model.SubCategory subCategory : subCategoryList)
        {
            itemMap.put(subCategory, getItemListBySubCategoryId(subCategory.id));
        }

        return itemMap;
    }

    /*
     * Shows the detail of item
     */
    private void dialogItemDetail(final com.ijul.projekpembunuh.model.Item item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuMakan.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_item_detail, null);

        final IncrementProductView incrementProductView = (IncrementProductView) view.findViewById(R.id.productView);
        TextView txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        final TextView txtUnitPrice = (TextView) view.findViewById(R.id.txtUnitPrice);
        final TextView txtExtendedPrice = (TextView) view.findViewById(R.id.txtExtendedPrice);
        final TextView txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
        final ImageView imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        txtItemName.setText(item.name);
        txtUnitPrice.setText(String.format("%.2f", item.unitPrice));
        txtQuantity.setText("1");
        txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * 1));

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DetailDialogAnimation;
        dialog.show();

        Glide.with(MenuMakan.this)
                .load(item.url)
                .into(imgThumbnail);

        incrementProductView.getAddIcon();

        incrementProductView.setOnStateListener(new OnStateListener()
        {
            @Override
            public void onCountChange(int count)
            {
                txtQuantity.setText(String.valueOf(count));
                txtExtendedPrice.setText(String.format("%.2f", item.unitPrice * count));
            }

            @Override
            public void onConfirm(int count)
            {

            }

            @Override
            public void onClose()
            {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addItemToCartAnimation(imgThumbnail, item, Integer.parseInt(txtQuantity.getText().toString()));

                dialog.dismiss();
            }
        });
    }


    private void addItemToCartAnimation(ImageView targetView, final com.ijul.projekpembunuh.model.Item item, final int quantity)
    {
        RelativeLayout destView = (RelativeLayout) findViewById(R.id.rlCart);

        new com.ijul.projekpembunuh.util.CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(300).setDestView(destView).setAnimationListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                addItemToCart(item, quantity);
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        }).startAnimation();
    }

    /*
     * Adds the item to order list.
     */
    private void addItemToCart(com.ijul.projekpembunuh.model.Item item, int quantity)
    {
        boolean isAdded = false;

        for (com.ijul.projekpembunuh.model.Order order : orderList)
        {
            if (order.item.id == item.id)
            {
                //if item already added to cart, dont add new order
                //just add the quantity
                isAdded = true;
                order.quantity += quantity;
                order.extendedPrice += item.unitPrice;
                break;
            }
        }

        //if item's not added yet
        if (!isAdded)
        {
            orderList.add(new com.ijul.projekpembunuh.model.Order(item, quantity));
        }

        orderAdapter.notifyDataSetChanged();
        rvOrder.smoothScrollToPosition(orderList.size() - 1);
        updateOrderTotal();
        updateBadge();
    }

    /*
     * Updates the value of the badge
     */
    private void updateBadge()
    {
        if (orderList.size() == 0)
        {
            txtCount.setVisibility(View.INVISIBLE);
        } else
        {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(orderList.size()));
        }
    }

    /*
     * Gets the total price of all products added to the cart
     */
    private double getOrderTotal()
    {
        double total = 0.0;

        for (com.ijul.projekpembunuh.model.Order order : orderList)
        {
            total += order.extendedPrice;
        }

        return total;
    }

    /*
     * Updates the total price of all products added to the cart
     */
    private void updateOrderTotal()
    {
        double total = getOrderTotal();
        txtTotal.setText(String.format("%.2f", total));
    }

    /*
     * Closes or opens the drawer
     */
    private void handleOrderDrawer()
    {
        if (drawer != null)
        {
            if (drawer.isDrawerOpen(GravityCompat.END))
            {
                drawer.closeDrawer(GravityCompat.END);
            } else
            {
                drawer.openDrawer(GravityCompat.END);
            }
        }
    }

    /*
     * Makes the cart empty
     */
    private void dialogClearAll()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(MenuMakan.this, android.R.style.Theme_Material_Dialog_Alert);
        } else
        {
            builder = new AlertDialog.Builder(MenuMakan.this);
        }
        builder.setTitle(R.string.clear_all)
                .setMessage(R.string.delete_all_orders)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        clearAll();
                        showMessage(true, getString(R.string.cart_clean));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                })
                .show();
    }

    /*
     * Completes the order
     */
    private void dialogCompleteOrder()
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(MenuMakan.this, android.R.style.Theme_Material_Dialog_Alert);
        } else
        {
            builder = new AlertDialog.Builder(MenuMakan.this);
        }
        builder.setTitle(getString(R.string.complete_order))
                .setMessage(getString(R.string.complete_order_question))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        showProgress(true);
                        new CompleteOrderTask().execute();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                })
                .show();
    }

    public class CompleteOrderTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            showProgress(false);
            clearAll();
            showMessage(true, getString(R.string.sent_order));
        }

        @Override
        protected void onCancelled()
        {
            showProgress(false);
            showMessage(false, getString(R.string.failed_order));
        }
    }

    private void showProgress(boolean show)
    {
        if (dialog == null)
        {
            dialog = new ProgressDialog(MenuMakan.this);
            dialog.setMessage(getString(R.string.sending_order));
        }

        if (show)
        {
            dialog.show();
        } else
        {
            dialog.dismiss();
        }
    }

    /*
     * Clears all orders from the cart
     */
    private void clearAll()
    {
        orderList.clear();
        orderAdapter.notifyDataSetChanged();

        updateBadge();
        updateOrderTotal();
        handleOrderDrawer();
    }

    /*
     * Shows a message by using Snackbar
     */
    private void showMessage(Boolean isSuccessful, String message)
    {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        if (isSuccessful)
        {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MenuMakan.this, R.color.colorAccent));
        } else
        {
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(MenuMakan.this, R.color.colorPomegranate));
        }

        snackbar.show();
    }
}
