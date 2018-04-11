/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        Node(Point point) {
            this.point = point;
            this.prev = this;
            this.next = this;
        }

        private void insertAfter(Node node) {
            if (node == null) {
                return;
            }

            //Update new allocated node connection
            this.next = node.next;
            this.prev = node;

            //Remove the connection to original next node
            //and update next to new allocated node
            node.next = this;

            if (head.prev == head) {
                head.prev = node;
            }
        }
    }

    Node head;

    public void insertBeginning(Point p) {
        Node node = new Node(p);
        if (isEmpty()) {
            head = node;
        } else {
            node.insertAfter(head);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        int count = 0;
        for (Point p : this) {
            count++;
        }
        return count;
    }

    public Point begin() {
        return (head == null)? null : head.point;
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Point previous = null;
        for (Point p : this) {
            if (previous != null) {
                total += distanceBetween(previous, p);
            }
            previous = p;
        }

        return total;
    }

    public void insertNearest(Point p) {
        Node node = new Node(p);
        if (isEmpty()) {
            head = node;
        } else {
            float minimumDistance = Float.MAX_VALUE;
            Node nearestNode = null;
            Node it = head;

            do {
                float distance = distanceBetween(p, it.point);
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestNode = it;
                }
            } while (it != head);

            node.insertAfter(it);
        }
    }

    public void insertSmallest(Point p) {
        Node node = new Node(p);
        if (isEmpty()) {
            head = node;
        } else if (size() == 1) {
            node.insertAfter(head);
        } else {
            float smallestDistance = Float.MAX_VALUE;
            Node smallestNode = null;

            for (Node it = head.next; it != head; it = it.next) {
                float addedDistance = distanceBetween(it.point, p)
                    + distanceBetween(p, it.next.point) - distanceBetween(it.point, it.next.point);

                if (addedDistance < smallestDistance) {
                    smallestDistance = addedDistance;
                    smallestNode = it;
                }
            }

            Node it = head;
            do {
                float addedDistance = distanceBetween(it.point, p)
                    + distanceBetween(p, it.next.point) - distanceBetween(it.point, it.next.point);

                if (addedDistance < smallestDistance) {
                    smallestDistance = addedDistance;
                    smallestNode = it;
                }
                it = it.next;
            } while(it != head);

            node.insertAfter(smallestNode);
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
