package com.example.du_an1.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an1.R;
import com.example.du_an1.model.Don;
import com.example.du_an1.model.DonHang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter_choduyet extends RecyclerView.Adapter<Adapter_choduyet.ViewHolder> {
    List<DonHang> list_donHang;
    Context context;
    FirebaseFirestore db;
    int manhinh = 0;


    public Adapter_choduyet(List<DonHang> list_donHang, Context context, int i) {
        this.list_donHang = list_donHang;
        this.context = context;
        db = FirebaseFirestore.getInstance();
        manhinh = i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(((Activity) context).getLayoutInflater().
                inflate(R.layout.item_chodon, parent, false));
    }

    @SuppressLint({"RecyclerView", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Xanh = "#89FF00";
        String Do = "#FF0000";
        String Cam = "#FFC107";

        Long soluong = TongGiaSP(position);
        holder.tenSP.setText("Mã hàng: " + list_donHang.get(position).getMaDon());
        holder.giaSP.setText("Tổng giá: " + list_donHang.get(position).getGiaDon() + "đ");
        holder.soLuong.setText("Số lượng: " + soluong + " SP");
        holder.ngay.setText("Ngày mua: " + list_donHang.get(position).getNgayMua());
        if (list_donHang.get(position).getTrangThai() == 0) {
            holder.trangthai.setText("Đang chờ xác nhận");
            holder.trangthai.setTextColor(Color.parseColor(Cam));
            holder.xoa.setVisibility(View.VISIBLE);
        } else if (list_donHang.get(position).getTrangThai() == 1) {
            holder.trangthai.setText("Đã xác nhận đơn");
            holder.xoa.setVisibility(View.GONE);
            holder.trangthai.setTextColor(Color.parseColor(Xanh));
        } else if (list_donHang.get(position).getTrangThai() == 3) {
            holder.trangthai.setText("Đơn hàng bị từ chối");
            holder.trangthai.setTextColor(Color.parseColor(Do));
            holder.xoa.setVisibility(View.VISIBLE);
        } else {
            holder.trangthai.setText("Lỗi");
        }

        holder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xoa(list_donHang.get(position).getMaDon(), position);
            }
        });


    }


    private Long TongGiaSP(int p) {
        Long i = 0l;
        for (Don g : list_donHang.get(p).getListSP()) {
            i += g.getSoLuong();
        }
        return i;
    }

    private void xoa(String maDon, int p) {

        db.collection("donHang").document(maDon).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                    list_donHang.remove(p);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list_donHang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenSP, soLuong, giaSP, trangthai, xoa, ngay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenSP = itemView.findViewById(R.id.tv_tensp_gioHang);
            giaSP = itemView.findViewById(R.id.tv_giasp_giohang);
            soLuong = itemView.findViewById(R.id.tv_soluongsp_giohang);
            trangthai = itemView.findViewById(R.id.tv_mua_giohang);
            ngay = itemView.findViewById(R.id.tv_ngay_mua);
            xoa = itemView.findViewById(R.id.tv_xoa_giohang);
        }
    }
}
