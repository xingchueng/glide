package com.bumptech.glide.samples.flickr;

import android.content.Context;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.samples.flickr.api.Api;
import com.bumptech.glide.samples.flickr.api.Photo;

import java.io.InputStream;

/**
 * An implementation of ModelStreamLoader that leverages the StreamOpener class and the ExecutorService backing the
 * Engine to download the image and resize it in memory before saving the resized version
 * directly to the disk cache.
 */
public class FlickrModelLoader extends BaseGlideUrlLoader<Photo> {

    /**
     * The default factory for {@link com.bumptech.glide.samples.flickr.FlickrModelLoader}s.
     */
    public static class Factory implements ModelLoaderFactory<Photo, InputStream> {
        private final ModelCache<Photo, GlideUrl> modelCache = new ModelCache<Photo, GlideUrl>(500);

        @Override
        public ModelLoader<Photo, InputStream> build(Context context, MultiModelLoaderFactory multiFactory) {
            // TODO: fixme.
            return new FlickrModelLoader(multiFactory.build(GlideUrl.class, InputStream.class).get(0), modelCache);
        }

        @Override
        public void teardown() { }
    }

    public FlickrModelLoader(ModelLoader<GlideUrl, InputStream> urlLoader, ModelCache<Photo, GlideUrl> modelCache) {
        super(urlLoader, modelCache);
    }

    @Override
    public boolean handles(Photo model) {
        return true;
    }

    @Override
    protected String getUrl(Photo model, int width, int height) {
        return Api.getPhotoURL(model, width, height);
    }
}