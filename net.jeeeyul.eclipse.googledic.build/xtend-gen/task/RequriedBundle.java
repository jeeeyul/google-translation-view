package task;

import com.google.common.base.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class RequriedBundle {
  private String id;
  
  private String version;
  
  private Map<String,String> others = new Function0<Map<String,String>>() {
    public Map<String,String> apply() {
      HashMap<String,String> _hashMap = new HashMap<String,String>();
      return _hashMap;
    }
  }.apply();
  
  public RequriedBundle(final String expression) {
    String _trim = expression.trim();
    String[] _split = _trim.split(";");
    List<String> parse = IterableExtensions.<String>toList(((Iterable<? extends String>)Conversions.doWrapArray(_split)));
    String _get = parse.get(0);
    String _trim_1 = _get.trim();
    this.id = _trim_1;
    int _size = parse.size();
    boolean _greaterThan = (_size > 1);
    if (_greaterThan) {
      int _size_1 = parse.size();
      int _minus = (_size_1 - 1);
      IntegerRange _upTo = new IntegerRange(1, _minus);
      for (final Integer i : _upTo) {
        {
          String each = parse.get((i).intValue());
          String[] set = each.split("=");
          final String[] _converted_set = (String[])set;
          String _get_1 = ((List<String>)Conversions.doWrapArray(_converted_set)).get(0);
          String key = _get_1.trim();
          final String[] _converted_set_1 = (String[])set;
          String _get_2 = ((List<String>)Conversions.doWrapArray(_converted_set_1)).get(1);
          String value = _get_2.trim();
          boolean _equals = Objects.equal(key, "bundle-version");
          if (_equals) {
            this.version = value;
          } else {
            String _trim_2 = key.trim();
            String _trim_3 = value.trim();
            this.others.put(_trim_2, _trim_3);
          }
        }
      }
    }
  }
  
  public String setVersion(final String version) {
    String _version = this.version = version;
    return _version;
  }
  
  public String getVersion() {
    return this.version;
  }
  
  public String getId() {
    return this.id;
  }
  
  public CharSequence toFileContent() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.id, "");
    {
      boolean _notEquals = (!Objects.equal(this.version, null));
      if (_notEquals) {
        _builder.append(";bundle-version=");
        _builder.append(this.version, "");
      }
    }
    {
      Set<String> _keySet = this.others.keySet();
      boolean _hasElements = false;
      for(final String key : _keySet) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append(";", "");
        } else {
          _builder.appendImmediate(";", "");
        }
        _builder.append(key, "");
        _builder.append("=");
        String _get = this.others.get(key);
        _builder.append(_get, "");
      }
    }
    return _builder;
  }
  
  public String unquote(final String string) {
    String _trim = string.trim();
    int _length = string.length();
    int _minus = (_length - 1);
    return _trim.substring(1, _minus);
  }
  
  public static void main(final String[] args) {
    RequriedBundle _requriedBundle = new RequriedBundle("test;bundle-version=\"1.2\";x=2;y=3");
    RequriedBundle rb = _requriedBundle;
    CharSequence _fileContent = rb.toFileContent();
    InputOutput.<CharSequence>println(_fileContent);
  }
}
