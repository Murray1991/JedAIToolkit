/*
* Copyright [2016-2020] [George Papadakis (gpapadis@yahoo.gr)]
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
package org.scify.jedai.datamodel;

import java.io.Serializable;

import java.util.List;

import org.scify.jedai.utilities.TooManyComparisonsException;
import org.scify.jedai.utilities.IConstants;

/**
 *
 * @author G.A.P. II
 */
public class SimilarityPairs implements IConstants, Serializable {

    private final boolean isCleanCleanER;
    private int currentIndex;
    private final float[] similarities;
    private final int[] entityIds1;
    private final int[] entityIds2;

    public SimilarityPairs(boolean ccer, int comparisons) {
        currentIndex = 0;
        isCleanCleanER = ccer;
        entityIds1 = new int[comparisons];
        entityIds2 = new int[comparisons];
        similarities = new float[comparisons];
    }
    
    public SimilarityPairs(boolean ccer, List<AbstractBlock> blocks) {
        currentIndex = 0;
        isCleanCleanER = ccer;
        int totalComparisons = countComparisons(blocks);
        entityIds1 = new int[totalComparisons];
        entityIds2 = new int[totalComparisons];
        similarities = new float[totalComparisons];
    }

    public void addComparison(Comparison comparison) {
        entityIds1[currentIndex] = comparison.getEntityId1();
        entityIds2[currentIndex] = comparison.getEntityId2();
        similarities[currentIndex++] = comparison.getUtilityMeasure();
    }

    private int countComparisons(List<AbstractBlock> blocks) {
        long comparisons = blocks.stream().mapToLong(AbstractBlock::getNoOfComparisons).sum();

        if (MAX_COMPARISONS < comparisons) {
            throw new TooManyComparisonsException(comparisons);
        }

        return (int) comparisons;
    }

    public int[] getEntityIds1() {
        return entityIds1;
    }

    public int[] getEntityIds2() {
        return entityIds2;
    }

    public int getNoOfComparisons() {
        return currentIndex;
    }

    public PairIterator getPairIterator() {
        return new PairIterator(this);
    }

    public float[] getSimilarities() {
        return similarities;
    }

    public boolean isCleanCleanER() {
        return isCleanCleanER;
    }
}
