package com.example.busarrivaltime.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.busarrivaltime.R;
//import com.example.busarrivaltime.ui.main.RouteFragment.OnListFragmentInteractionListener;
import com.example.busarrivaltime.ui.main.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

///**
// * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Route> mRoutes;
//    private final List<DummyItem> mValues;
    private final OnListInteractionListener mListener;

    public RouteRecyclerViewAdapter(ArrayList<Route> routes, OnListInteractionListener listener) {
        mRoutes = routes;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mRoute = mRoutes.get(position);
//        holder.mIndex = position;
        holder.mIdView.setText(mRoutes.get(position).mBus);
        holder.mContentView.setText(mRoutes.get(position).mDescription);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListInteraction(holder.getAdapterPosition(), holder.mRoute);
                }
            }
        });

        holder.mEditView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onEditButtonInteraction(holder.getAdapterPosition());
                }
            }
        });


//        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button mEditView;
        public Route mRoute;
//        public int mIndex;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mEditView = (Button) view.findViewById(R.id.edit);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnListInteractionListener {
        // TODO: Update argument type and name
        void onListInteraction(int index, Route route);
        void onEditButtonInteraction(int index);
    }
}
