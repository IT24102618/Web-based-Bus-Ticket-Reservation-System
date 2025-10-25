-- Migration script to add district column to Route table
-- This script adds the district column to support district-based route management

-- Add district column to Route table
ALTER TABLE Route 
ADD COLUMN district VARCHAR(50) NOT NULL DEFAULT 'Colombo';

-- Update existing routes with default district (you may want to update these manually based on your data)
-- Example: UPDATE Route SET district = 'Galle' WHERE from_stand LIKE '%Galle%' OR to_stand LIKE '%Galle%';

-- Add index for better performance when querying by district
CREATE INDEX idx_route_district ON Route(district);

-- Add composite index for district and from_stand for better query performance
CREATE INDEX idx_route_district_from_stand ON Route(district, from_stand);
