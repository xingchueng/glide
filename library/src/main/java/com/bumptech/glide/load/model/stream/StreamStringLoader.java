package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.StringLoader;

import java.io.InputStream;

/**
 * A {@link ModelLoader} for translating {@link String} models, such as file paths or remote urls, into
 * {@link InputStream} data.
 */
public class StreamStringLoader extends StringLoader<InputStream> implements StreamModelLoader<String> {

    /**
     * The default factory for {@link com.bumptech.glide.load.model.stream.StreamStringLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<String, InputStream> {
        @Override
        public ModelLoader<String, InputStream> build(Context context, MultiModelLoaderFactory multiFactory) {
            // TODO: fixme.
            return new StreamStringLoader(multiFactory.build(Uri.class, InputStream.class).get(0));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    protected StreamStringLoader(ModelLoader<Uri, InputStream> uriLoader) {
        super(uriLoader);
    }
}