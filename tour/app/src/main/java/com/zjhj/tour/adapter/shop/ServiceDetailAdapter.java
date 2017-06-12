package com.zjhj.tour.adapter.shop;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhj.commom.result.IndexData;
import com.zjhj.commom.result.ShopBasicResult;
import com.zjhj.commom.result.ShopPhoneResult;
import com.zjhj.commom.result.ShopPolicyResult;
import com.zjhj.commom.result.ShopServiceResult;
import com.zjhj.commom.util.DPUtil;
import com.zjhj.tour.R;
import com.zjhj.tour.interfaces.RecyOnItemClickListener;
import com.zjhj.tour.widget.DividerListItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by brain on 2017/5/20.
 */
public class ServiceDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int BASIC = 0;
    private final static int POLICY = 1;
    private final static int PHONE = 2;
    private final static int SERVICE = 3;
    private final static int DIVIDER = 4;

    LayoutInflater inflater;

    private List<IndexData> mList;
    private Context mContext;
    private RecyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ServiceDetailAdapter(Context context, List<IndexData> list) {
        inflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BASIC:
                return new BASICViewHolder(inflater.inflate(R.layout.layout_service_basic, parent, false));
            case POLICY:
                return new POLICYViewHolder(inflater.inflate(R.layout.layout_service_policy, parent, false));
            case SERVICE:
                return new SERVICEViewHolder(inflater.inflate(R.layout.layout_service_service, parent, false));
            case PHONE:
                return new PHONEViewHolder(inflater.inflate(R.layout.layout_service_phone, parent, false));
            case DIVIDER:
                return new DividerViewHolder(inflater.inflate(R.layout.layout_divider_hight_five, parent, false));
            default:
                return new BASICViewHolder(inflater.inflate(R.layout.layout_service_basic, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BASICViewHolder) {
            setBasic((BASICViewHolder) holder, position);
        } else if (holder instanceof POLICYViewHolder) {
            setPolicy((POLICYViewHolder) holder, position);
        } else if (holder instanceof PHONEViewHolder) {
            setPhone((PHONEViewHolder) holder, position);
        } else if (holder instanceof SERVICEViewHolder) {
            setService((SERVICEViewHolder) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).getType()) {
            case "BASIC":
                return BASIC;
            case "POLICY":
                return POLICY;
            case "PHONE":
                return PHONE;
            case "SERVICE":
                return SERVICE;
            case "DIVIDER":
                return DIVIDER;
            default:
                return BASIC;
        }
    }

    class BASICViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.name_ll)
        LinearLayout nameLl;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.address_ll)
        LinearLayout addressLl;
        @Bind(R.id.desc)
        TextView desc;
        @Bind(R.id.desc_ll)
        LinearLayout descLl;
        @Bind(R.id.population_max)
        TextView populationMax;
        @Bind(R.id.population_max_ll)
        LinearLayout populationMaxLl;
        @Bind(R.id.parking_location)
        TextView parkingLocation;
        @Bind(R.id.parking_location_ll)
        LinearLayout parkingLocationLl;

        public BASICViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class POLICYViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.accept_booking_time)
        TextView acceptBookingTime;
        @Bind(R.id.accept_booking_time_ll)
        LinearLayout acceptBookingTimeLl;
        @Bind(R.id.booking_time)
        TextView bookingTime;
        @Bind(R.id.booking_time_ll)
        LinearLayout bookingTimeLl;
        @Bind(R.id.children_price)
        TextView childrenPrice;
        @Bind(R.id.children_price_ll)
        LinearLayout childrenPriceLl;
        @Bind(R.id.pay_type)
        TextView payType;
        @Bind(R.id.pay_type_ll)
        LinearLayout payTypeLl;
        @Bind(R.id.dining_time)
        TextView diningTime;
        @Bind(R.id.dining_time_ll)
        LinearLayout diningTimeLl;

        public POLICYViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SERVICEViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;
        public SERVICEViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class PHONEViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tel)
        TextView tel;
        @Bind(R.id.tel_ll)
        LinearLayout telLl;
        @Bind(R.id.mobile)
        TextView mobile;
        @Bind(R.id.mobile_ll)
        LinearLayout mobileLl;

        public PHONEViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setBasic(BASICViewHolder holder, int position) {
        ShopBasicResult shopBasicResult = (ShopBasicResult) mList.get(position).getData();
        if (TextUtils.isEmpty(shopBasicResult.getName())) {
            holder.nameLl.setVisibility(View.GONE);
        } else {
            holder.nameLl.setVisibility(View.VISIBLE);
            holder.name.setText(shopBasicResult.getName());
        }

        if (TextUtils.isEmpty(shopBasicResult.getAddress())) {
            holder.addressLl.setVisibility(View.GONE);
        } else {
            holder.addressLl.setVisibility(View.VISIBLE);
            holder.address.setText(shopBasicResult.getAddress());
        }

        if (TextUtils.isEmpty(shopBasicResult.getDesc())) {
            holder.descLl.setVisibility(View.GONE);
        } else {
            holder.descLl.setVisibility(View.VISIBLE);
            holder.desc.setText(shopBasicResult.getDesc());
        }

        if (TextUtils.isEmpty(shopBasicResult.getPopulation_max())) {
            holder.populationMaxLl.setVisibility(View.GONE);
        } else {
            holder.populationMaxLl.setVisibility(View.VISIBLE);
            holder.populationMax.setText(shopBasicResult.getPopulation_max());
        }

        if (TextUtils.isEmpty(shopBasicResult.getParking_location())) {
            holder.parkingLocationLl.setVisibility(View.GONE);
        } else {
            holder.parkingLocationLl.setVisibility(View.VISIBLE);
            holder.parkingLocation.setText(shopBasicResult.getParking_location());
        }

    }

    private void setPolicy(POLICYViewHolder holder, int position) {

        ShopPolicyResult shopPolicyResult = (ShopPolicyResult) mList.get(position).getData();
        if (TextUtils.isEmpty(shopPolicyResult.getAccept_booking_time())) {
            holder.acceptBookingTimeLl.setVisibility(View.GONE);
        } else {
            holder.acceptBookingTimeLl.setVisibility(View.VISIBLE);
            holder.acceptBookingTime.setText(shopPolicyResult.getAccept_booking_time());
        }

        if (TextUtils.isEmpty(shopPolicyResult.getBooking_time())) {
            holder.bookingTimeLl.setVisibility(View.GONE);
        } else {
            holder.bookingTimeLl.setVisibility(View.VISIBLE);
            holder.bookingTime.setText(shopPolicyResult.getBooking_time());
        }

        if (TextUtils.isEmpty(shopPolicyResult.getChildren_price())) {
            holder.childrenPriceLl.setVisibility(View.GONE);
        } else {
            holder.childrenPriceLl.setVisibility(View.VISIBLE);
            holder.childrenPrice.setText(shopPolicyResult.getChildren_price());
        }

        if (TextUtils.isEmpty(shopPolicyResult.getPay_type())) {
            holder.payTypeLl.setVisibility(View.GONE);
        } else {
            holder.payTypeLl.setVisibility(View.VISIBLE);
            holder.payType.setText(shopPolicyResult.getPay_type());
        }

        if (TextUtils.isEmpty(shopPolicyResult.getDining_time())) {
            holder.diningTimeLl.setVisibility(View.GONE);
        } else {
            holder.diningTimeLl.setVisibility(View.VISIBLE);
            holder.diningTime.setText(shopPolicyResult.getDining_time());
        }

    }

    private void setPhone(PHONEViewHolder holder, int position) {
        ShopPhoneResult shopPhoneResult = (ShopPhoneResult) mList.get(position).getData();
        if (TextUtils.isEmpty(shopPhoneResult.getTel())) {
            holder.telLl.setVisibility(View.GONE);
        } else {
            holder.telLl.setVisibility(View.VISIBLE);
            holder.tel.setText(shopPhoneResult.getTel());
        }

        if (TextUtils.isEmpty(shopPhoneResult.getMobile())) {
            holder.mobileLl.setVisibility(View.GONE);
        } else {
            holder.mobileLl.setVisibility(View.VISIBLE);
            holder.mobile.setText(shopPhoneResult.getMobile());
        }

    }

    private void setService(SERVICEViewHolder holder, int position) {
        List<ShopServiceResult> serviceResults = (List<ShopServiceResult>) mList.get(position).getData();

        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        holder.recyclerView.setLayoutManager(manager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.addItemDecoration(new DividerListItemDecoration(mContext, OrientationHelper.HORIZONTAL, DPUtil.dip2px(4), mContext.getResources().getColor(R.color.shop_white)));
        ServiceItemAdapter mAdapter = new ServiceItemAdapter(mContext,serviceResults);
        holder.recyclerView.setAdapter(mAdapter);

    }

}
