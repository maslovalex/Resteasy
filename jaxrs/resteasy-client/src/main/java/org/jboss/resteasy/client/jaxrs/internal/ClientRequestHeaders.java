package org.jboss.resteasy.client.jaxrs.internal;

import org.jboss.resteasy.spi.NotImplementedYetException;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.jboss.resteasy.util.DateUtil;
import org.jboss.resteasy.util.HeaderHelper;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ClientRequestHeaders
{
   protected CaseInsensitiveMap<Object> headers = new CaseInsensitiveMap<Object>();
   protected Locale language;
   protected MediaType mediaType;
   protected List<MediaType> accepts = new ArrayList<MediaType>();
   protected List<Locale> acceptableLanguages = new ArrayList<Locale>();
   protected Map<String, Cookie> cookies = new HashMap<String, Cookie>();
   protected ResteasyProviderFactory providerFactory;

   public ClientRequestHeaders(ResteasyProviderFactory providerFactory)
   {
      this.providerFactory = providerFactory;
   }

   public ClientRequestHeaders clone()
   {
      ClientRequestHeaders copy = new ClientRequestHeaders(providerFactory);
      copy.headers.putAll(headers);
      copy.language = language;
      copy.accepts.addAll(accepts);
      copy.mediaType = mediaType;
      copy.cookies.putAll(cookies);
      return copy;
   }

   public CaseInsensitiveMap<Object> getHeaders()
   {
      return headers;
   }

   public void setLanguage(Locale language)
   {
      header("Language", language);
      this.language = language;
   }

   public void setLanguage(String language)
   {
      setLanguage(new Locale(language));
   }

   public void setMediaType(MediaType mediaType)
   {
      header("Content-Type", mediaType);
      this.mediaType = mediaType;
   }

   public void acceptLanguage(Locale... locales)
   {
      headers.remove(HttpHeaders.ACCEPT_LANGUAGE);
      acceptableLanguages = new ArrayList<Locale>();
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (Locale l : locales)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         acceptableLanguages.add(l);
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT_LANGUAGE, builder.toString());
   }

   public void acceptLanguage(String... locales)
   {
      headers.remove(HttpHeaders.ACCEPT_LANGUAGE);
      acceptableLanguages = new ArrayList<Locale>();
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (String l : locales)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         acceptableLanguages.add(new Locale(l));
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT_LANGUAGE, builder.toString());
   }

   public void accept(String... types)
   {
      headers.remove(HttpHeaders.ACCEPT);
      accepts = new ArrayList<MediaType>();
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (String l : types)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         accepts.add(MediaType.valueOf(l));
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT, builder.toString());
   }

   public void accept(MediaType... types)
   {
      headers.remove(HttpHeaders.ACCEPT);
      accepts = new ArrayList<MediaType>();
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (MediaType l : types)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         accepts.add(l);
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT, builder.toString());
   }

   public void cookie(Cookie cookie)
   {
      cookies.put(cookie.getName(), cookie);
      headers.add(HttpHeaders.COOKIE, cookie);
   }

   public void allow(String... methods)
   {
      HeaderHelper.setAllow(this.headers, methods);
   }

   public void allow(Set<String> methods)
   {
      HeaderHelper.setAllow(headers, methods);
   }

   public void cacheControl(CacheControl cacheControl)
   {
      headers.putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
   }

   public void header(String name, Object value)
   {
      if (value == null)
      {
         headers.remove(name);
         return;
      }
      headers.add(name, value);
   }

   public Set<Link> getLinks()
   {
      throw new NotImplementedYetException();
   }

   public boolean hasLink(String relation)
   {
      throw new NotImplementedYetException();
   }

   public Link getLink(String relation)
   {
      throw new NotImplementedYetException();
   }

   public Link.Builder getLinkBuilder(String relation)
   {
      throw new NotImplementedYetException();
   }

   public Date getDate()
   {
      Object d = headers.getFirst(HttpHeaders.DATE);
      if (d == null) return null;
      if (d instanceof Date) return (Date) d;
      return DateUtil.parseDate(d.toString());
   }

   public String getHeader(String name)
   {
      Object val = headers.getFirst(name);
      if (val == null) return null;

      return HeaderHelper.toHeaderString(val, providerFactory);
   }

   public MultivaluedMap<String, String> asMap()
   {
      CaseInsensitiveMap<String> map = new CaseInsensitiveMap<String>();
      for (Map.Entry<String, List<Object>> entry : headers.entrySet())
      {
         for (Object obj : entry.getValue())
         {
            map.add(entry.getKey(), providerFactory.toHeaderString(obj));
         }
      }
      return map;
   }

   public List<String> getHeaderValues(String name)
   {
      List<Object> vals = headers.get(name);
      if (vals == null) return new ArrayList<String>();
      List<String> values = new ArrayList<String>();
      for (Object val : vals)
      {
         values.add(providerFactory.toHeaderString(val));
      }
      return values;
   }

   public Locale getLanguage()
   {
      return language;
   }

   public int getLength()
   {
      return -1;
   }

   public MediaType getMediaType()
   {
      return mediaType;
   }

   public List<MediaType> getAcceptableMediaTypes()
   {
      return accepts;
   }

   public List<Locale> getAcceptableLanguages()
   {
      return acceptableLanguages;
   }

   public Map<String, Cookie> getCookies()
   {
      return cookies;
   }
}
