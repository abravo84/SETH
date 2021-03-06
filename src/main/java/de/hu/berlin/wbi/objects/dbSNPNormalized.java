package de.hu.berlin.wbi.objects;

/**
 Copyright 2010, 2011 Philippe Thomas
 This file is part of snp-normalizer.

 snp-normalizer is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 snp-normalizer is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with snp-normalizer.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.EnumSet;

/**
 * Represents a {@link MutationMention} mention which has been successfully
 * normalized to a {@link dbSNP} . Therefore it extends {@link dbSNP}.
 * 
 * @author Philippe Thomas
 * 
 */
public class dbSNPNormalized extends dbSNP implements Comparable<dbSNPNormalized>{

    /**
	 * {@link MutationMention} mention is derived using a specific uniprotfeature
	 */
	private UniprotFeature feature = null;
		
    private EnumSet<MatchOptions> matchType;

    public EnumSet<MatchOptions> getMatchType() { return matchType; }

	/**
	 * @param dbsnp          dbSNP Object
	 * @param feature        UniProt feature used for normalization
     * @param matchType      EnumSet of MatchOptions enum values
	 */
	public dbSNPNormalized(dbSNP dbsnp, EnumSet<MatchOptions> matchType, UniprotFeature feature)
    {
		super();
		this.rsID = dbsnp.getRsID();
		this.geneID = dbsnp.getGeneID();
		this.residues = dbsnp.getResidues();
		this.aaPosition = dbsnp.getAaPosition();
		this.hgvs = dbsnp.getHgvs();
        this.matchType = matchType;

	}

    /**
     * @return confidence for Normalization
     */
    public int getConfidence() {
        int conf = 0;

        conf += isPsm() ? 3 : 2;
        conf += isExactMatch() ? 2 : 0;

        return conf;
    }

	/**
	 * (non-Javadoc)
	 * 
	 * @see de.hu.berlin.wbi.objects.dbSNP#toString()
	 */
	@Override
	public String toString() {
		return "dbSNPNormalized [rsID=" + rsID + ", Contig="
				+ " ,GeneID " + geneID 
				+ ", residues=" + residues + " ,aaPosition=" + aaPosition + "]";

	}
	
	/**
	 * Returns true if Alleles are in the same order in dbSNP as in the text mention
	 */
	public boolean isAlleleOrder() {
		return (!matchType.contains(MatchOptions.SWAPPED));
	}

	/**
	 * @return true if the normalization required no heuristics 
	 */
	public boolean isExactMatch() {
		return (matchType.contains(MatchOptions.LOC)
            && !matchType.contains(MatchOptions.SWAPPED));
	}

	/**
	 * @return true if position has an offset of +/-1 but otherwise there's a match
	 */
	public boolean isMethioneMatch() {
		return (matchType.contains(MatchOptions.METHIONE));
	}
	
	public boolean isFeatureMatch(){
		return feature != null;
	}

    public UniprotFeature getFeature() {
        return feature;
    }

    /**
	 * @return true if snp is a protein sequence mutation
	 */
	public boolean isPsm() {
		return matchType.contains(MatchOptions.PSM);
	}
	
	@Override
	public int compareTo(dbSNPNormalized that) {
		return that.getConfidence() - this.getConfidence();
    }
}
