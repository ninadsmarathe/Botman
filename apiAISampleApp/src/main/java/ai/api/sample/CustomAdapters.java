package ai.api.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


/**
 * Created by abc on 7/11/2022.
 */
public class CustomAdapters  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Information> data= Collections.emptyList();
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private LayoutInflater inflater;
    private Context context;
    public CustomAdapters(Context context, List<Information> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.custom_row, parent,false);
        HeaderHolder holder=new HeaderHolder(view);
        return holder;



    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder ){

        }
        else{
            ItemHolder itemHolder= (ItemHolder) holder;
            Information current=data.get(position-1);
            itemHolder.title.setText(current.title);
            itemHolder.icon.setImageResource(current.iconId);
        }

    }
    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon= (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);

        }
    }
}

