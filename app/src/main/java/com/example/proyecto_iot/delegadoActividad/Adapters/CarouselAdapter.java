package com.example.proyecto_iot.delegadoActividad.Adapters;

import static com.google.android.material.animation.AnimationUtils.lerp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoActividad.CarouselModel;
import com.google.android.material.carousel.MaskableFrameLayout;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private List<CarouselModel> list;
    private Context context;

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.carousel_layout, parent, false);
        return new CarouselViewHolder(view);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        CarouselModel carousel = getList().get(position);
        holder.carousel = carousel;

        ImageView image = holder.itemView.findViewById(R.id.carousel_image_view);
        image.setImageURI(carousel.getImageUri());
        MaskableFrameLayout container = holder.itemView.findViewById(R.id.carousel_item_container);
        TextView textView = holder.itemView.findViewById(R.id.carouselText);
        container.setOnMaskChangedListener(maskRect -> {
            textView.setTranslationX(maskRect.left);
            textView.setAlpha(lerp(1F, 0F, 0F, 80F, maskRect.left));
        });
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder{
        CarouselModel carousel;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<CarouselModel> getList() {
        return list;
    }

    public void setList(List<CarouselModel> list) {
        this.list = list;
    }
}
