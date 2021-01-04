package ziz.org.ecommerce.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ziz.org.ecommerce.Interface.ItemClickListner;
import ziz.org.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /** le nom du produit */
    public TextView txtProductName;
    /** la description de produit */
    public TextView txtProductDesciption;
    /** le prix du produit */
    public TextView txtProductPrice;
    /** l'image du produit */
    public ImageView imageView;

    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDesciption = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View v)
    {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
