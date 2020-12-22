/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2021, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.crawl;

import static java.util.Comparator.naturalOrder;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import schemacrawler.schema.NamedObject;
import schemacrawler.schema.ReducibleCollection;
import us.fatehi.utility.ObjectToString;

/**
 * Ordered list of named objects, that can be searched associatively. NamedObjectList has the
 * ability to look up by dependent object which is not created yet. That is, by NamedObject +
 * String. Returns values sorted in natural sort order, and is iterable. The iterator does not allow
 * modifications to the underlying data structure.
 */
final class NamedObjectList<N extends NamedObject> implements Serializable, ReducibleCollection<N> {

  private static final long serialVersionUID = 3257847666804142128L;

  private static List<String> makeLookupKey(final NamedObject namedObject) {
    final List<String> key;
    if (namedObject == null) {
      key = null;
    } else {
      key = namedObject.toUniqueLookupKey();
    }
    return key;
  }

  private static List<String> makeLookupKey(final NamedObject namedObject, final String name) {
    final List<String> key = makeLookupKey(namedObject);
    if (key != null) {
      key.add(name);
    }
    return key;
  }

  private final Map<List<String>, N> objects = new HashMap<>();

  @Override
  public void filter(final Predicate<? super N> predicate) {
    if (predicate == null) {
      return;
    }

    final Set<Entry<List<String>, N>> entrySet = objects.entrySet();
    for (final Iterator<Entry<List<String>, N>> iterator = entrySet.iterator();
        iterator.hasNext(); ) {
      final Entry<List<String>, N> entry = iterator.next();
      if (!predicate.test(entry.getValue())) {
        iterator.remove();
      }
    }
  }

  @Override
  public boolean isFiltered(final NamedObject object) {
    return !contains(object);
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<N> iterator() {
    final class UnmodifiableIterator implements Iterator<N> {

      private final Iterator<N> iterator;

      UnmodifiableIterator(final Iterator<N> iterator) {
        this.iterator = iterator;
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public N next() {
        return iterator.next();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
    return new UnmodifiableIterator(values().iterator());
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return ObjectToString.toString(values());
  }

  /**
   * Add a named object to the list.
   *
   * @param namedObject Named object
   */
  boolean add(final N namedObject) {
    requireNonNull(namedObject, "Cannot add a null object to the list");
    final List<String> key = makeLookupKey(namedObject);
    objects.put(key, namedObject);
    return true;
  }

  boolean contains(final NamedObject namedObject) {
    return objects.containsKey(makeLookupKey(namedObject));
  }

  boolean isEmpty() {
    return objects.isEmpty();
  }

  /**
   * Looks up a named object by lookup key.
   *
   * @param lookupKey Internal lookup key
   * @return Named object
   */
  Optional<N> lookup(final List<String> lookupKey) {
    return internalGet(lookupKey);
  }

  Optional<N> lookup(final NamedObject namedObject, final String name) {
    final List<String> key = makeLookupKey(namedObject, name);
    return internalGet(key);
  }

  N remove(final N namedObject) {
    return objects.remove(makeLookupKey(namedObject));
  }

  /**
   * Returns the number of elements in this list.
   *
   * @return Number of elements in this list.
   */
  int size() {
    return objects.size();
  }

  /**
   * Gets all named objects in the list, in sorted order.
   *
   * @return All named objects
   */
  List<N> values() {
    final List<N> all = new ArrayList<>(objects.values());
    all.sort(naturalOrder());
    return all;
  }

  private Optional<N> internalGet(final List<String> key) {
    return Optional.ofNullable(objects.get(key));
  }
}
