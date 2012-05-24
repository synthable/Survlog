package com.grasscove.survlog.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.grasscove.survlog.R;
import com.grasscove.survlog.provider.SurvlogContract.Hosts;
import com.grasscove.survlog.ui.MainActivity;
import com.grasscove.survlog.ui.phone.HostActivity;

public class HostsFragment extends BaseListFragment {

	public static interface HostsFragmentCallback {
		public void onHostClick(long id);
	}

	public static final HostsFragment newInstance() {
		return new HostsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = new SimpleCursorAdapter(
			getActivity(), android.R.layout.simple_list_item_2,
			null,
			new String[] { Hosts.Columns.TITLE, Hosts.Columns.HOST },
			new int[] { android.R.id.text1, android.R.id.text2 },
		    0
		);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerForContextMenu(getListView());

		getListView().setBackgroundColor(Color.TRANSPARENT);
		getListView().setCacheColorHint(Color.TRANSPARENT);

		getLoaderManager().initLoader(HOSTS, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		((MainActivity)getActivity()).onHostClick(id);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.host_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final Uri uri = Hosts.buildHostUri(info.id);
		switch(item.getItemId()) {
			default:
			case R.id.menu_edit:
				startActivity(new Intent(
					Intent.ACTION_EDIT,
					uri,
					getActivity(),
					HostActivity.class
				));
				break;
			case R.id.menu_delete:
				getActivity().getContentResolver().delete(uri, null, null);
				break;
		}

		return super.onContextItemSelected(item);
	}

	/*private static final class HostsAdapter extends SimpleCursorAdapter {

        private static final class ViewHolder {
        	RelativeLayout root;
            TextView name;
            TextView symbol;
            TextView category;
        }

        private static final String[] from = new String[] {
            Hosts.Columns.TITLE, Hosts.Columns.HOST
        };
        private static final int[] to = new int[] {
        	android.R.id.text1, android.R.id.text2
        };

        private LayoutInflater inflater;

        public HostsAdapter(Context context, Cursor c) {
            super(context, android.R.layout.simple_list_item_2, c, from, to, 0);
            inflater = LayoutInflater.from(context);
        }*/

        /*@Override
        public Host getItem(int position) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            return new Host(cursor);
        }*/

        /*@Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            View view = inflater.inflate(R.layout.strain_item, parent, false);

            holder.root = (RelativeLayout) view.findViewById(R.id.strain_root);
            holder.name = (TextView) view.findViewById(R.id.strain_name_view);
            holder.symbol = (TextView) view.findViewById(R.id.strain_symbol_view);
            holder.category = (TextView) view.findViewById(R.id.strain_category_view);

            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();

            String category = cursor.getString(cursor.getColumnIndex(Strains.Columns.CATEGORY));

            holder.name.setText(cursor.getString(cursor.getColumnIndex(Strains.Columns.NAME)));
            holder.symbol.setText(cursor.getString(cursor.getColumnIndex(Strains.Columns.SYMBOL)));

            if(category.equals("Hybrid")) {
            	holder.root.setBackgroundResource(R.drawable.hybrid_tile);
            } else if(category.equals("Indica")) {
            	holder.root.setBackgroundResource(R.drawable.indica_tile);
            } else if(category.equals("Sativa")) {
            	holder.root.setBackgroundResource(R.drawable.sativa_tile);
            }

            holder.category.setText(category);
        }*/
    //}
}