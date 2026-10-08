-- Memastikan line_order tidak boleh duplikat dalam satu mapping
-- Setiap coa_mapping harus memiliki urutan line yang unik
ALTER TABLE coa_mapping_lines
ADD CONSTRAINT uq_mapping_line_order
UNIQUE (mapping_id, line_order); 