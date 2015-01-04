package com.bumptech.glide.load.model.stream;

import android.content.Context;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.UrlLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * A wrapper class that translates {@link java.net.URL} objects into {@link com.bumptech.glide.load.model.GlideUrl}
 * objects and then uses the wrapped {@link com.bumptech.glide.load.model.ModelLoader} for
 * {@link com.bumptech.glide.load.model.GlideUrl}s to load the {@link java.io.InputStream} data.
 */
public class StreamUrlLoader extends UrlLoader<InputStream> {

    /**
     * The default factory for {@link com.bumptech.glide.load.model.stream.StreamUrlLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<URL, InputStream> {
        @Override
        public ModelLoader<URL, InputStream> build(Context context, MultiModelLoaderFactory multiFactory) {
            // TODO: fixme.
            return new StreamUrlLoader(multiFactory.build(GlideUrl.class, InputStream.class).get(0));
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    public StreamUrlLoader(ModelLoader<GlideUrl, InputStream> glideUrlLoader) {
        super(glideUrlLoader);
    }
}