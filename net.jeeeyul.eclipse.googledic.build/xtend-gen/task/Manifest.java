package task;

import com.google.common.base.Objects;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.ManifestElement;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import task.OrderedMap;
import task.RequriedBundle;

/**
 * ±úÁø°Å ÀÖ³ª?
 */
@SuppressWarnings("all")
public class Manifest {
  private IFile file;
  
  private OrderedMap header = new Function0<OrderedMap>() {
    public OrderedMap apply() {
      OrderedMap _orderedMap = new OrderedMap();
      return _orderedMap;
    }
  }.apply();
  
  public Manifest(final IFile file) {
    try {
      this.file = file;
      InputStream _contents = file.getContents();
      ManifestElement.parseBundleManifest(_contents, this.header);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String getBundleId() {
    String _get = this.header.get("Bundle-SymbolicName");
    String[] _split = _get.split(";");
    String _head = IterableExtensions.<String>head(((Iterable<String>)Conversions.doWrapArray(_split)));
    return _head;
  }
  
  public String setBundleVersion(final String verString) {
    String _put = this.header.put("Bundle-Version", verString);
    return _put;
  }
  
  public void updateDependencies(final Collection<String> bundleIdList, final String version) {
    String dependencies = this.header.get("Require-Bundle");
    boolean _equals = Objects.equal(dependencies, null);
    if (_equals) {
      return;
    }
    String[] _split = dependencies.split(",");
    List<String> depList = IterableExtensions.<String>toList(((Iterable<? extends String>)Conversions.doWrapArray(_split)));
    ArrayList<RequriedBundle> _arrayList = new ArrayList<RequriedBundle>();
    final ArrayList<RequriedBundle> reqBundleList = _arrayList;
    final Procedure1<String> _function = new Procedure1<String>() {
        public void apply(final String it) {
          RequriedBundle _requriedBundle = new RequriedBundle(it);
          reqBundleList.add(_requriedBundle);
        }
      };
    IterableExtensions.<String>forEach(depList, _function);
    final Function1<RequriedBundle,Boolean> _function_1 = new Function1<RequriedBundle,Boolean>() {
        public Boolean apply(final RequriedBundle it) {
          String _id = it.getId();
          boolean _contains = bundleIdList.contains(_id);
          return Boolean.valueOf(_contains);
        }
      };
    Iterable<RequriedBundle> _filter = IterableExtensions.<RequriedBundle>filter(reqBundleList, _function_1);
    final Procedure1<RequriedBundle> _function_2 = new Procedure1<RequriedBundle>() {
        public void apply(final RequriedBundle it) {
          it.setVersion(version);
        }
      };
    IterableExtensions.<RequriedBundle>forEach(_filter, _function_2);
    final Function1<RequriedBundle,CharSequence> _function_3 = new Function1<RequriedBundle,CharSequence>() {
        public CharSequence apply(final RequriedBundle it) {
          CharSequence _fileContent = it.toFileContent();
          return _fileContent;
        }
      };
    String newValue = IterableExtensions.<RequriedBundle>join(reqBundleList, ",\r\n ", _function_3);
    this.header.put("Require-Bundle", newValue);
  }
  
  public CharSequence toFileContent() {
    StringConcatenation _builder = new StringConcatenation();
    {
      List<String> _keyList = this.header.getKeyList();
      for(final String key : _keyList) {
        _builder.append(key, "");
        _builder.append(": ");
        String _get = this.header.get(key);
        CharSequence _lineFormat = this.lineFormat(_get);
        _builder.append(_lineFormat, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence lineFormat(final String s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String[] _split = s.split(",");
      boolean _hasElements = false;
      for(final String each : _split) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",\r\n ", "");
        }
        String _replaceAll = each.replaceAll("(\\r\\n)+", "");
        String _trim = _replaceAll.trim();
        _builder.append(_trim, "");
      }
    }
    return _builder;
  }
  
  public void save() {
    try {
      CharSequence _fileContent = this.toFileContent();
      String _string = _fileContent.toString();
      byte[] _bytes = _string.getBytes("UTF-8");
      ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(_bytes);
      this.file.setContents(_byteArrayInputStream, true, true, null);
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
